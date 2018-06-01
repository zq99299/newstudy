package cn.mrcode.newstudy.hpbase._10;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程版本TelnetEchoServer
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/29 23:25
 */
public class TelnetEchoServer {
    public static void main(String[] args) throws IOException {
        new TelnetEchoServer().start(9658);
    }

    private ExecutorService executorService = Executors.newWorkStealingPool();

    public void start(int port) throws IOException {

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        // 服务器只接收accept
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 就绪选择
            /**
             * 该方法返回情况：
             *  1. 4个操作任意一个就绪
             *  2. wakeup(): 利用Sink pipe设置一个信号量，发送一个特定的数据，唤醒select过程，立即返回
             */

            int selectNum = selector.select();
            // 获取
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                try {
                    /**
                     * 移除 和 读的关系：
                     * 1.只读不移除：select返回之后如果客户端没有发送数据过来，则读取不到数据
                     * 2.只移除不读：如果继续注册读事件，select返回之后，那么看到的现象就是不断的被唤醒读
                     * 所以：需要读和移除 才是正确的处理方法
                     */
                    it.remove();
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        readable(key);
                    } else if (key.isWritable()) {
                        writable(key);
                    } else {
                        System.out.println("不处理的：" + key);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取消key，下一次操作key将失效
                    key.cancel();
                    // 已取消的key也能获取通道，使用关闭通道
                    IOUtils.closeQuietly(key.channel());
                }
            }
        }
    }


    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        // 可以通过该方法设置通道相关的配置
        // 比如 接收缓冲区大小
//        sc.setOption(StandardSocketOptions.SO_RCVBUF,100);
        // 发送/write缓冲区大小
//        sc.setOption(StandardSocketOptions.SO_SNDBUF,100);
        sc.register(key.selector(), SelectionKey.OP_READ);
        sc.write(ByteBuffer.wrap("welcome to you\r\n".getBytes()));
    }

    private void readable(SelectionKey key) {
        // 更改兴趣，暂停对该key的读事件通知
        // 但是系统还会接收该key的数据
        // 下次处理读事件则可以无阻塞读取
        key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
        executorService.submit(() -> {
            try {
                System.out.println("received read event");
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(100);
                socketChannel.read(buffer);
                if (key.attachment() != null) {
                    // 因为同一个通道 使用的key是同一个
                    // 使用这里需要等待上一次写完之后 才能继续写数据
                    System.out.println("还有未写完的数据，当次忽略处理");
                    key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                    key.selector().wakeup();
                    return;
                }
                int sendBufferSize = socketChannel.socket().getSendBufferSize();
                System.out.println("send buffer size:" + sendBufferSize);
                buffer = ByteBuffer.allocate(sendBufferSize * 50 + 2);
                for (int i = 0; i < buffer.capacity() - 2; i++) {
                    buffer.put((byte) ('a' + i % 25));
                }
                buffer.put("\r\n".getBytes());
                buffer.flip();
                HashMap<String, Object> attach = new HashMap<>();
                attach.put("writedTotal", 0);
                attach.put("buffer", buffer);
                key.attach(attach);
                // 恢复读兴趣，增加写兴趣
                key.interestOps(key.interestOps() | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                key.selector().wakeup();
            } catch (Exception e) {
                System.out.println("捕获异常‘" + e.getMessage() + "' 关闭通道");
                // TODO: 2018/5/30 测试下关闭通道是否会取消key
                IOUtils.closeQuietly(key.channel());
                // 让选择器在阻塞状态中立即恢复
                key.selector().wakeup();
            }
        });
    }


    private void writable(SelectionKey key) {
        key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
        executorService.submit(() -> {
            try {
                System.out.println("reveived write event");
                SocketChannel socketChannel = (SocketChannel) key.channel();
                Map<String, Object> attachment = (Map<String, Object>) key.attachment();
                if (attachment != null) {
                    write(key, socketChannel, attachment);
                }
                key.selector().wakeup();
            } catch (Exception e) {
                System.out.println("捕获异常‘" + e.getMessage() + "' 关闭通道");
                IOUtils.closeQuietly(key.channel());
                key.selector().wakeup();
            }
        });
    }

    private void write(SelectionKey key, SocketChannel socketChannel, Map<String, Object> attach) throws IOException {
        int writedTotal = (int) attach.get("writedTotal");
        ByteBuffer buffer = (ByteBuffer) attach.get("buffer");
        int writed = socketChannel.write(buffer);
        writedTotal += writed;
        System.out.println("writed " + writed);
        if (buffer.hasRemaining()) {
            System.out.println(" not write finished ,remains " + buffer.remaining() + " writedTotal " + writedTotal);
            // 压缩buffer；丢弃已使用数据，把未使用数据复制到从0开始处
            // pos = 剩余数量
            buffer = buffer.compact();
            buffer.flip();
            attach.put("writedTotal", writedTotal);
            attach.put("buffer", buffer);
            // 继续需要写事件
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        } else {
            System.out.println(" ====== block write finished: writedTotal " + writedTotal + " buffer cap " + buffer.capacity());
            key.attach(null);
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }
}
