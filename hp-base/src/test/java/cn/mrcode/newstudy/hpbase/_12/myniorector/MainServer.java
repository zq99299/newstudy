package cn.mrcode.newstudy.hpbase._12.myniorector;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 按照已学知识点；自己实现一个简单的通用nio rector 架子
 * <p>
 * 组成：
 * acceptor : 处理链接
 * nioRector : 处理选择事件；与 数据的读写处理；
 * decode: 解码链； 解决固定的自定义协议处理，比如按行分割读取到的命令；
 * encod: 编码链：解决固定的自定义协议处理，比如追加一个换行符号标识以结尾
 * iohandler：相关事件的回调
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/14 22:34
 */
public class MainServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        MyNIORector[] rectors = new MyNIORector[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < rectors.length; i++) {
            rectors[i] = new MyNIORector(executor);
        }

        MyNIOAcceptor acceptor = new MyNIOAcceptor(9000, rectors);
        acceptor.start();
    }
}
