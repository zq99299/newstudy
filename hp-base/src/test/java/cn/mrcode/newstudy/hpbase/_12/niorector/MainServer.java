package cn.mrcode.newstudy.hpbase._12.niorector;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 程序入口; 多rector版 telnet
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/12 22:16
 */
public class MainServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        // 有几个cpu就创建几个rector
        MyNIORector[] rectors = new MyNIORector[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < rectors.length; i++) {
            rectors[i] = new MyNIORector(executor);
            rectors[i].start();
        }

        // 一个请求接收处理器
        // 对于服务端来说，就是一个ServerSocket用于监听客户端的链接请求
        NIOAcceptor acceptor = new NIOAcceptor(9000, rectors);
        acceptor.start();
    }
}
