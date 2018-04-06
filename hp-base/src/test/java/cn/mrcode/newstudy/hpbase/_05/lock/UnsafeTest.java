package cn.mrcode.newstudy.hpbase._05.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/2 23:55
 */
public class UnsafeTest {
    public static void main(String[] args) throws InterruptedException {
        new Unsafe1().start();
    }

    public static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 没有看懂，只能让线程可见，下一次读可见。但是不能保证同步
     */
    static class Unsafe1 {
        private int a;
        private int b;
        private int c;

        public void start() throws InterruptedException {
            Thread t1 = new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    b = 1;
                    a = 2;
                    System.out.println(Thread.currentThread().getName() + " a ,b已被修改");
                    getUnsafe().storeFence();
                    TimeUnit.SECONDS.sleep(1);
                    c = 3;
                    System.out.println(Thread.currentThread().getName() + " c 已被修改");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            Thread t2 = new Thread(() -> {
                while (a != 2) {
//                    try {
//                        TimeUnit.MILLISECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    getUnsafe().loadFence();
                }
                System.out.println(b + " , " + c);
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }
    }
}
