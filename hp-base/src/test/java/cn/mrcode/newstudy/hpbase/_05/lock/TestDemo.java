package cn.mrcode.newstudy.hpbase._05.lock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/28 22:28
 */
public class TestDemo {
    @org.junit.Test
    public void fun1() {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .parallel()
                .filter(s -> {
                    System.out.println(Thread.currentThread().getName() + " filter: " + s);
                    return true;
                })
                .forEach(s -> System.out.println(Thread.currentThread().getName() + " forEach: " + s));

    }

    private volatile int a;
    private int b;
    private int c;

    @Test
    public void fun2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                b = 1;
                c = 3;
                a = 2;
                System.out.println(Thread.currentThread().getName() + " a 已被修改");
                System.out.println(Thread.currentThread().getName() + " b,c 已被修改");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            while (a != 2) {
            }

            System.out.println(b + " , " + c);
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
