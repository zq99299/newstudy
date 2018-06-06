package cn.mrcode.newstudy.hpbase._11.singlethread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 单线程版本的 reactor
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/5 23:37
 */
public class NIORector {
    private Selector selector;

    public void start(int port) throws IOException {
        selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(port));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int select = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                if (sk.isAcceptable()) {
                    new Acceptor(sk).run();
                } else {
                    ((IoHandler) sk.attachment()).run();
                }
            }
            selectionKeys.clear();
        }
    }
}
