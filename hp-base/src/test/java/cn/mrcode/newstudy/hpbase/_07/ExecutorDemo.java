package cn.mrcode.newstudy.hpbase._07;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class ExecutorDemo {
    public static void main(String[] args) throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        List<Future<Long>> collect = Files.list(Paths.get("D:/"))
                .filter(i -> !i.toFile().isDirectory()) // 过滤掉文件夹
                .map(file -> (Callable<Long>) () -> Files.size(file)) // 统计所有文件的大小
                .map(c -> es.submit(c))
                .collect(Collectors.toList());
        Supplier<LongStream> ls = () -> (collect
                .stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return -1L;
                }).mapToLong(i -> i));
        ls.get().forEach(System.out::println);
        System.out.println(ls.get().sum());
        es.shutdown();
    }

    /**
     * 线程池复用原理：
     * Thread.start开启线程；然后会调用run方法。运行完成则线程则自动销毁
     * 那么怎么让线程复用呢?  很简单：让线程的run方法不停的执行 Runnable（Runnable来源就是线程池中的排队队列）
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        MyPool myPool = new MyPool();
        IntStream.range(0, 5)
                .mapToObj(i -> (Runnable) () -> {
                    System.out.println(Thread.currentThread().getName() + " : " + i);
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .forEach(r -> {
                    myPool.execute(r);
                });
        synchronized (this) {
            this.wait();
        }
    }

    public class MyPool {
        ArrayBlockingQueue<Runnable> works = new ArrayBlockingQueue<>(10);
        private Work work;

        public synchronized void execute(Runnable runnable) {
            if (work == null) {
                work = new Work(runnable, this);
                work.start();
            }
            try {
                works.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Work extends Thread {
        private Runnable work;
        private MyPool pool;

        public Work(Runnable work, MyPool pool) {
            this.work = work;
            this.pool = pool;
        }

        @Override
        public void run() {
            while (true) {
                work = pool.works.poll();
                if (work == null) {
                    break;
                }
                work.run();
                work = null;
            }
        }
    }
}
