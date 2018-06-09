package cn.mrcode.newstudy.hpbase._11.multithreading;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/08     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/8 16:01
 * @date 2018/6/8 16:01
 * @since 1.0.0
 */
public class TestDemo {
    public static void main(String[] args) throws IOException {
        new RectorServer(9000).start();
    }


    @Test
    public void fun2() throws IOException, InterruptedException {
        List<Thread> ts = IntStream.range(0, 3).mapToObj(i -> {
            Thread thread = new Thread(() -> new DownloadFileClient().start(9000));
            thread.start();
            return thread;
        }).collect(Collectors.toList());
        for (Thread t : ts) {
            t.join();
        }
    }
}
