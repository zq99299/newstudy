package cn.mrcode.newstudy.hpbase._11.multithreading;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/08     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/8 16:02
 * @date 2018/6/8 16:02
 * @since 1.0.0
 */
public class RectorServer extends Thread {
    private int port;
    private Selector selector;
    //    private ExecutorService acceptEs = Executors.newCachedThreadPool();
    private ExecutorService otherEs = Executors.newCachedThreadPool();

    public RectorServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            selector = Selector.open();
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress(port));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                int selectNum = selector.select();
                System.out.println("就绪事件：" + selectNum);
                Set<SelectionKey> sks = selector.selectedKeys();
                Iterator<SelectionKey> it = sks.iterator();
                while (it.hasNext()) {
                    SelectionKey sk = it.next();
                    if (sk.isAcceptable()) {
                        // 接受链接，并关联handler
                        doAccept(sk);
                        it.remove();
                        continue;
                    }
                    if (sk.isReadable()) {
                        // 更改兴趣，防止多次处理同一个事件
                        sk.interestOps(sk.interestOps() & ~SelectionKey.OP_READ);
                        otherEs.submit((IoHandler) sk.attachment());
                        it.remove();
                    } else if (sk.isWritable()) {
                        sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
                        otherEs.submit((IoHandler) sk.attachment());
                        it.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doAccept(SelectionKey sk) throws IOException {
        // 因为同一时间sk只会有一个，那么在多线程处理中会不会出现问题
        ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
        SocketChannel accept = ssc.accept();
        otherEs.submit(new IoHandler(selector, accept));
    }
}
