package cn.mrcode.newstudy.temptest;

import java.util.stream.IntStream;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/08     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/8 10:27
 * @date 2017/12/8 10:27
 * @since 1.0.0
 */
public class Test {
    public static void main(String[] args) {
        doIt();
    }
    public static void doIt() {
        IntStream.range(0, 1).forEach(i -> {
            System.out.println(Thread.currentThread().getName() + " 生产 " + i);
        });
    }
}