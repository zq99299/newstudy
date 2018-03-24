package cn.mrcode.newstudy.hpbase._04.functioninterface;

import org.junit.Test;

import java.util.function.IntConsumer;

/**
 * 使用拉姆达表达式编程
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/24 21:27
 */
public class Lambda {
    /**
     * 多次重复执行一个操作； 提供不同版本的参数
     */
    @Test
    public void fun1() {
        repeat(10, i -> System.out.println(i));
        repeat(10, () -> System.out.println("xxx"));
    }

    /**
     * 多次重复执行一个操作；
     * @param n
     * @param consumer 使用intConsumer的好处的可以回调的时候捕获参数
     */
    public static void repeat(int n, IntConsumer consumer) {
        for (int i = 0; i < n; i++) {
            consumer.accept(i);
        }
    }

    /**
     * 多次重复执行一个操作；
     * @param n
     * @param consumer 使用runnable在回调的时候不能捕获参数
     */
    public static void repeat(int n, Runnable consumer) {
        for (int i = 0; i < n; i++) {
            consumer.run();
        }
    }
}
