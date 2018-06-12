package cn.mrcode.newstudy.hpbase._12.niorector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/12 22:21
 */
public class NIOAcceptor extends Thread {
    private final ServerSocketChannel ssc;
    private final MyNIORector[] rectors;
    int count = 0;

    public NIOAcceptor(int port, MyNIORector[] rectors) throws IOException {
        this.rectors = rectors;
        ssc = ServerSocketChannel.open();
        ssc.configureBlocking(true);  // 阻塞模式
        InetSocketAddress address = new InetSocketAddress(port);
        ssc.bind(address);
        System.out.println("started at " + address);
    }

    @Override
    public void run() {
        int length = rectors.length;
        // 只接收链接请求
        while (true) {
            try {
                SocketChannel sc = ssc.accept();
                System.out.println("Connection Accepted " + sc.getRemoteAddress());
                int nextReator = ++count % length;
                rectors[nextReator].registerNewClient(sc);
                if (count >= 10000) {
                    count = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
