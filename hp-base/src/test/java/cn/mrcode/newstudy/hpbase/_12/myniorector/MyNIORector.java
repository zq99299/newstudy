package cn.mrcode.newstudy.hpbase._12.myniorector;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
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

    public MyNIORector(ExecutorService executor) throws IOException {
        this.selector = Selector.open();
        this.executor = executor;
    }

    public void registerNewClient(SocketChannel socketChannel) throws IOException {
        System.out.println(String.format("%s %s 新注册", Thread.currentThread().getName(), socketChannel.getRemoteAddress()));
        socketChannel.configureBlocking(false);
        SelectionKey sk = socketChannel.register(selector, 0);
        // 需要配置业务处理器 - 然后获取在这里实例化
        MyIOHandler myIOHandler = new MyIOHandler(sk);
        executor.submit(myIOHandler);
    }

    @Override
    public void run() {

    }
}
