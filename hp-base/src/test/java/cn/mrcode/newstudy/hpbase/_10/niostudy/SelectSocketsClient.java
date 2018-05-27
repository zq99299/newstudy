package cn.mrcode.newstudy.hpbase._10.niostudy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/27 23:06
 */
public class SelectSocketsClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        new SelectSocketsClient().start(9857);
    }

    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    private void start(int port) throws IOException, InterruptedException {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        socketChannel.write(ByteBuffer.wrap("hello word 1\r\n".getBytes()));
        TimeUnit.MILLISECONDS.sleep(500);
        socketChannel.write(ByteBuffer.wrap("hello word 2\r\n".getBytes()));
        TimeUnit.MILLISECONDS.sleep(500);
        socketChannel.write(ByteBuffer.wrap("hello word 3\r\n".getBytes()));
        while (selector.select() > 0) {
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                SocketChannel channel = (SocketChannel) key.channel();
                buffer.clear();
                channel.read(buffer);
                buffer.flip();
                System.out.println(Charset.forName("UTF-8").decode(buffer));
                it.remove();
            }
        }
    }


}
