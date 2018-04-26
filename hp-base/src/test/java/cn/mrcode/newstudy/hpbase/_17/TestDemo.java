package cn.mrcode.newstudy.hpbase._17;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/8 22:03
 */
public class TestDemo {
    private String lock = "lock";

    @Test
    public void fun1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            // 1. lock上的锁定动作数量为1
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + " - 开始等待");
                    // 2. 该线程被添加到等待集中
                    // 3. 但是在lock上执行n个解锁动作;此时lock上的锁定动作为0
                    lock.wait();
                    System.out.println(Thread.currentThread().getName() + " - 被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            // 4. 如果这里不添加一个锁定动作，那么将会抛出illegalMonitor异常
            // 5. 这里添加一个锁定动作，所以 锁定动作数量为1
            synchronized (lock) {
                // 6. 当锁定动作数量大于0的时候，等待集中的线程被唤醒（被移除等待集）一个（具体是哪一个 不知道）
                lock.notify();
                System.out.println(Thread.currentThread().getName() + " - 唤醒线程");
            }
        });
        t1.start();
        TimeUnit.MILLISECONDS.sleep(500);  // 防止t2先运行
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void fun2() throws InterruptedException {
        new StringFinal().start();
    }

    /**
     * string 虽然设计成final的，但是下面的a是一个引用类型的变量；
     * 首页对引用类型的变量的改写，不会有final域的效果
     */
    static class StringFinal {
        private String a;
        private int b;
        private int c;

        public void start() throws InterruptedException {
            Thread t1 = new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    a = "ok";
                    b = 2;
                    c = 2;
                    System.out.println(Thread.currentThread().getName() + " c 已被修改");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            Thread t2 = new Thread(() -> {
                while (!"ok".equals(a)) {
                }
                System.out.println(b + " , " + c);
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }
    }

    // 字撕裂 测试
    @Test
    public void fun3() throws InterruptedException {
        new WordTearing(2).start();
    }

    static class WordTearing extends Thread {
        static final int LENGTH = 8;
        static final int ITERS = 100_0000;
        static byte[] counts = new byte[LENGTH];
        static Thread[] threads = new Thread[LENGTH];

        final int id;

        WordTearing(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            byte v = 0;
            for (int i = 0; i < ITERS; i++) {
                byte v2 = counts[id];
                if (v != v2) {
                    System.err.println(
                            "word - tearing found: " +
                                    "counts[" + id + "] = " +
                                    v2 +
                                    ", shoule be " + v
                    );
                    v++;
                    counts[id] = v;
                }
            }
        }
    }
}
