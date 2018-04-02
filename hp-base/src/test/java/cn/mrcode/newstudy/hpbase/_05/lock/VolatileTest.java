package cn.mrcode.newstudy.hpbase._05.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/2 22:54
 */
public class VolatileTest {
    public static void main(String[] args) throws InterruptedException {
//        new NoVolatile().start();
//        new Volatile().start();
        new Volatile2().start();
    }

    /**
     * 演示 工作区和主存通信是异步的，
     * a线程修改共享变量a
     * b线程无限循环，等待共享变量a的修改，然后打印出
     * 但是 有可能永远都无法得到修改后的值；
     */
    static class NoVolatile {
        private int a;
        private int b;
        private int c;

        public void start() throws InterruptedException {
            Thread t1 = new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    a = 2;
                    b = 1;
                    c = 3;
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

    /**
     * 和前面代码一样；只是把 共享变量a增加了 volatile
     * 演示：对 volatile的写会在前后插入store屏障；
     * 线程b能得到修改后的值
     */
    static class Volatile {
        private volatile int a;
        private int b;
        private int c;

        public void start() throws InterruptedException {
            Thread t1 = new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    a = 2;
                    b = 1;
                    c = 3;
                    // 这里由于是连续的写，所以很大功能a后面的操作也会被另外一个线程读取到；只是可能
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName() + " a 已被修改");
                    System.out.println(Thread.currentThread().getName() + " b,c 已被修改");
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

    /**
     * 演示 store，栅栏前的修改都会被推入到主存中；
     * 正确结果应该是 线程结束；并打印1，0
     */
    static class Volatile2 {
        private volatile int a;
        private int b;
        private int c;

        public void start() throws InterruptedException {
            Thread t1 = new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    b = 1;
                    a = 2;
                    System.out.println(Thread.currentThread().getName() + " a ,b已被修改");
                    // 这里模拟了简断写，c一定不会被另外一个线程立即读取到
                    // 但是 b ，a 的修改一定可以被立即读取到
                    TimeUnit.SECONDS.sleep(1);
                    c = 3;
                    System.out.println(Thread.currentThread().getName() + " c 已被修改");
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
}
