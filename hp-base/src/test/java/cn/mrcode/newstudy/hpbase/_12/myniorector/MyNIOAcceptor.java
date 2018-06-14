package cn.mrcode.newstudy.hpbase._12.myniorector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 非阻塞模式接收处理
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/14 23:04
 */
public class MyNIOAcceptor extends Thread {
    private Selector selector;
    private ServerSocketChannel ssc;
    private final MyNIORector[] rectors;
    private final int rectorCount;
    private int count;

    public MyNIOAcceptor(int port, MyNIORector[] rectors) throws IOException {
        this.rectors = rectors;
        rectorCount = rectors.length;
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        InetSocketAddress isa = new InetSocketAddress(port);
        ssc.bind(isa);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务已启动 " + isa);
    }

    @Override
    public void run() {
        try {
            while (true) {

                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey sk = it.next();
                    if (!sk.isAcceptable()) {
                        continue;
                    }
                    // 处理链接平均分配到多个rector中去执行
                    rectors[++count % rectorCount].registerNewClient(ssc.accept());
                    if (count >= 100000) {
                        count = 0;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
