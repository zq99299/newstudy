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
                    iter.remove();
                } else if ((sk.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    SocketChannel socketChannel = (SocketChannel) sk.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(100);
                    buffer.put("\r\nFollo you:".getBytes());
                    socketChannel.read(buffer);
                    buffer.put("\r\n".getBytes());
                    buffer.flip();
                    socketChannel.write(buffer);

                }
            }
        }

    }
}
