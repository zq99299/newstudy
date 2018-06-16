package cn.mrcode.newstudy.hpbase._12.myniorector;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/15     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/15 13:35
 * @date 2018/6/15 13:35
 * @since 1.0.0
 */
public class Bootstrap {
    private Function<SelectionKey, MyIOHandler> handlerFactory;

    public Bootstrap handler(Function<SelectionKey, MyIOHandler> factory) {
        this.handlerFactory = factory;
        return this;
    }

    public void start(int port) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        MyNIORector[] rectors = new MyNIORector[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < rectors.length; i++) {
            rectors[i] = new MyNIORector(this, executor);
            rectors[i].start();
        }

        MyNIOAcceptor acceptor = new MyNIOAcceptor(port, rectors);
        acceptor.start();
    }

    public Function<SelectionKey, MyIOHandler> getHandlerFactory() {
        return handlerFactory;
    }
}
