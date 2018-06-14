package cn.mrcode.newstudy.hpbase._12.niorector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/12 22:21
 */
public class NIOAcceptorNonBlock extends Thread {
    private Selector selector;
    private final ServerSocketChannel ssc;
    private final MyNIORector[] rectors;
    int count = 0;

    public NIOAcceptorNonBlock(int port, MyNIORector[] rectors) throws IOException {
        this.rectors = rectors;
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);  // 阻塞模式
        InetSocketAddress address = new InetSocketAddress(port);
        ssc.bind(address);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("started at " + address);
    }

    @Override
    public void run() {
        int length = rectors.length;
        // 只接收链接请求
        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey sk = it.next();
                    if (sk.isAcceptable()) {
                        SocketChannel sc = ssc.accept();
                        System.out.println("Connection Accepted " + sc.getRemoteAddress());
                        int nextReator = ++count % length;
                        rectors[nextReator].registerNewClient(sc);
                        if (count >= 10000) {
                            count = 0;
                        }
                    }
                }
                selectionKeys.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
