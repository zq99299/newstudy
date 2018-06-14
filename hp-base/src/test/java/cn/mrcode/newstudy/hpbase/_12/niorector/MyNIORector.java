package cn.mrcode.newstudy.hpbase._12.niorector;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/12 22:17
 */
public class MyNIORector extends Thread {
    private final Selector selector;
    private final ExecutorService executor;

    public MyNIORector(ExecutorService executor) throws IOException {
        this.executor = executor;
        selector = Selector.open();
    }

    @Override
    public void run() {
        while (true) {
            Set<SelectionKey> selectionKeys = null;
            try {
                int selectNum = selector.select(500);
//                selector.select()
                selectionKeys = selector.selectedKeys();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (SelectionKey selectionKey : selectionKeys) {
                IOHandler ioHandler = (IOHandler) selectionKey.attachment();
                this.executor.execute(ioHandler);
            }
            selectionKeys.clear();
        }
    }

    public void registerNewClient(SocketChannel sc) throws IOException {
        System.out.println(" registered by actor " + this.getName());
        new TelnetIOHandler(selector, sc);
    }
}
