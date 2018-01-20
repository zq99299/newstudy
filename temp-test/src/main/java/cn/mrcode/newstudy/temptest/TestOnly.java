package cn.mrcode.newstudy.temptest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestOnly {
    static Object lock = new Object();
    static ArrayList<String> datas = new ArrayList<String>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = IntStream.range(1, 10).mapToObj(i -> {
            if (i % 2 == 0) {
                return new MThread("consumer " + i);
            } else {
                return new NThread("producer " + i);
            }
        }).filter(t -> {
            t.start();
            return true;
        }).collect(Collectors.toList());
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {

            }
        });

    }
}

// 消费者
class MThread extends Thread {
    public MThread(String string) {
        this.setName(string);
    }
    public void run() {
        while (true) {
            synchronized (TestOnly.lock) {
                if (TestOnly.datas.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + " 列表为空，等待数据的到来");
                    try {
                        TestOnly.lock.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + " 等待完成，消费数据 ");
                }
                if (TestOnly.datas.isEmpty()) {
                    // wati 被唤醒之后，会继续执行下面的逻辑，所以有可能被唤醒的是一个等待的线程，但是没有库存
//                    System.out.println("impossible empty !! " + Thread.currentThread().getName());
//                    System.exit(-1);
                } else {
                    TestOnly.datas.forEach(s -> System.out.println(Thread.currentThread().getName() + "消费 " + s));
                    TestOnly.datas.clear();
                }
                TestOnly.lock.notifyAll();
            }
        }
    }
}

// 生产者
class NThread extends Thread {
    public NThread(String string) {
        this.setName(string);
    }
    public void run() {
        while (true) {
            // 最多只能有2个库存，大于2个则程序有问题
            synchronized (TestOnly.lock) {
                if (TestOnly.datas.size() > 1) {
                    System.out.println(Thread.currentThread().getName() + " 列表还有 " + TestOnly.datas.size() + " 条数据，进入等待");
                    try {
                        TestOnly.lock.wait();
                    } catch (InterruptedException e) {
                        break;

                    }
                    System.out.println(Thread.currentThread().getName() + "等待完成，列表已被消费，剩余数据 " + TestOnly.datas.size() + " ，开始生产");
                }
                if (TestOnly.datas.size() > 1) {
                    // 同理 消费者一样，使用wait就有这个问题，因为被唤醒的顺序不能确定
//                    System.out.println("impossible full !! " + Thread.currentThread().getName());
//                    System.exit(-1);
                } else {
                    IntStream.range(0, 1).forEach(i -> {
                        System.out.println(Thread.currentThread().getName() + " 生产 " + i);
                        TestOnly.datas.add(i + " data");
                    });
                }
                TestOnly.lock.notifyAll();
            }
        }
    }
}

