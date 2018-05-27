package cn.mrcode.newstudy.hpbase._10.niostudy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 使用选择器来为多个通道服务程序实例研究
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/27 22:42
 */
public class SelectSockets {
    public static int PORT_NUMBER = 1234;
    // 但单线程中对所有通道使用一个缓冲区，不会有竞争条件
    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

    public static void main(String[] args) throws IOException {
        args = new String[]{"9857"};
        new SelectSockets().go(args);
    }

    public void go(String[] args) throws IOException {
        int port = PORT_NUMBER;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        System.out.println("Listeningon port " + port);
        InetSocketAddress isa = new InetSocketAddress(port);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(isa);
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int n = selector.select();
            if (n == 0) {
                continue;
            }
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    SocketChannel channel = ((ServerSocketChannel) key.channel())
                            .accept();
                    registerChannel(selector, channel, SelectionKey.OP_READ);
                    sayHello(channel);
                }
                if (key.isReadable()) {
                    readDataFromSocket(key);
                }
                // 移除表示已处理，否则下次还会获取到该key
                it.remove();
            }
        }
    }


    private void registerChannel(Selector selector, SocketChannel channel, int ops) throws IOException {
        if (channel == null) return;
        channel.configureBlocking(false);
        channel.register(selector, ops);
    }

    private void sayHello(SocketChannel channel) throws IOException {
        buffer.clear();
        buffer.put("Hi there!\r\n".getBytes());
        buffer.flip();
        channel.write(buffer);
    }

    protected void readDataFromSocket(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        buffer.clear();
        int count;
        while ((count = socketChannel.read(buffer)) > 0) {
            buffer.flip();
            // 传递什么数据过来，再发回去
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            buffer.clear();
        }
        if (count < 0) {
            // 关闭通道，让key失效？
            socketChannel.close();
        }
    }
}
