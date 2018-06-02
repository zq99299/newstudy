package cn.mrcode.newstudy.hpbase._10;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/20 22:40
 */
public class TestDemo {
    @Test
    public void fun1() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);  // 不阻塞
        ssc.socket().bind(new InetSocketAddress(7000));
        System.out.println("started at " + ssc.socket().getInetAddress());
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int selectNumber = selector.select();
            System.out.println("selectored Number is :" + selectNumber);
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey sk = iter.next();
                if ((sk.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) sk.channel();
                    SocketChannel accept = serverChannel.accept();
                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ);
                    accept.write(ByteBuffer.wrap("welcome to you\r\n".getBytes()));
                } else if ((sk.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    System.out.println("received read event");
                    SocketChannel socketChannel = (SocketChannel) sk.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(100);
                    socketChannel.read(buffer);
                    int sendBufferSize = socketChannel.socket().getSendBufferSize();
                    System.out.println("send buffer size:" + sendBufferSize);
                    buffer = ByteBuffer.allocate(sendBufferSize * 50 + 2);
                    for (int i = 0; i < buffer.capacity() - 2; i++) {
                        buffer.put((byte) ('a' + i % 25));
                    }
                    buffer.put("\r\n".getBytes());
                    buffer.flip();
                    write(sk, socketChannel, buffer);
                } else if (sk.isWritable()) {
                    System.out.println("reveived write event");
                    SocketChannel socketChannel = (SocketChannel) sk.channel();
                    ByteBuffer buffer = (ByteBuffer) sk.attachment();
                    if (buffer != null) {
                        write(sk, socketChannel, buffer);
                    }
                }
                iter.remove();
            }
        }

    }

    int writedTotal = 0;

    private void write(SelectionKey sk, SocketChannel socketChannel, ByteBuffer buffer) throws IOException {
        int writed = socketChannel.write(buffer);
        writedTotal += writed;
        System.out.println("writed " + writed);
        if (buffer.hasRemaining()) {
            System.out.println(" not write finished ,remains " + buffer.remaining());
            // 压缩buffer；丢弃已使用数据，把未使用数据复制到从0开始处
            // pos = 剩余数量
            buffer = buffer.compact();
            buffer.flip();
            sk.attach(buffer);
            sk.interestOps(sk.interestOps() | SelectionKey.OP_WRITE);
        } else {
            System.out.println(" block write finished: writedTotal " + writedTotal + " buffer cap " + buffer.capacity());
            sk.attach(null);
            sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }
}
