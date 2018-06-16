package cn.mrcode.newstudy.hpbase._12.myniorector;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * rector: 处理选择
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/14 23:17
 */
public class MyNIORector extends Thread {
    private Selector selector;
    private final ExecutorService executor;
    private final Bootstrap bootstrap;

    public MyNIORector(Bootstrap bootstrap, ExecutorService executor) throws IOException {
        this.selector = Selector.open();
        this.executor = executor;
        this.bootstrap = bootstrap;
    }

    public void registerNewClient(SocketChannel socketChannel) throws IOException {
        System.out.println(String.format("%s %s 新注册", Thread.currentThread().getName(), socketChannel.getRemoteAddress()));
        socketChannel.configureBlocking(false);
        selector.wakeup();
        SelectionKey sk = socketChannel.register(selector, 0);
        // 需要配置业务处理器 - 然后获取在这里实例化
        bootstrap.getHandlerFactory().apply(sk);
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
                    // 防止线程业务耗时执行，还未改变兴趣，select又返回了
                    // 造成执行线程暴增
                    if (sk.isReadable()) {
                        sk.interestOps(sk.interestOps() & ~SelectionKey.OP_READ);
                    } else if (sk.isWritable()) {
                        sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
                    }
                    executor.submit((MyIOHandler) sk.attachment());
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
