package cn.mrcode.newstudy.hpbase._11.mysinglethread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 单线程版本 挑战1；完全凭借总结的需求完成开发；开发不完成，绝不查看原版是怎么写的
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/6 23:38
 */
public class NioReactor extends Thread {
    private Selector selector;

    public NioReactor(int port) throws IOException {
        // 声明 ssc和相关配置
        selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(port));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        // 开始接收请求 和 就绪事件的分发
        while (true) {
            try {
                int selectNum = selector.select();
                System.out.println("selectNum " + selectNum);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey sk = it.next();
                    if (sk.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
                        SocketChannel accept = ssc.accept();
                        accept.configureBlocking(false);
                        new IoHandler(selector, accept);
                        it.remove();
                        continue;
                    }
                    if (sk.isReadable() || sk.isWritable()) {
                        ((IoHandler) sk.attachment()).run();
                        it.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
