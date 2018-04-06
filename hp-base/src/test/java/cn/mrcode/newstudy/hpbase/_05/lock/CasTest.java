package cn.mrcode.newstudy.hpbase._05.lock;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/28 22:49
 */
public class CasTest {
    private static final int MAX_THREADS = 3; // 线程数量
    private static final int TASK_COUNT = 3; // 任务数
    private static final int TARGET_COUNT = 10000; // 目标总数

    public static class Sync {
        private long count = 0;

        private synchronized long inc() {
            return ++count;
        }

        private synchronized long getCount() {
            return count;
        }

        public void clearCount() {
            count = 0;
        }
    }

    @Test
    public void test() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);
        Instant start = Instant.now();
        CountDownLatch cd = new CountDownLatch(TASK_COUNT);

        Sync sync = new Sync();
        Runnable t = () -> {
            long v = sync.getCount();
            while (v < TARGET_COUNT) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                v = sync.inc();
            }
            System.out.println("sync:" + Duration.between(start, Instant.now()).toMillis() + ",count=" + v);
            cd.countDown();
        };
        IntStream.range(0, MAX_THREADS).forEach(i -> {
            service.submit(t);
        });
        cd.await();
        service.shutdown();
    }

    public static class Cas {
        private long count = 0;

        private long inc() {
            for (; ; ) {
                long temp = this.count;
                long target = temp + 1;
                if (compareAndSwap(count, temp, target)) {
                    return target;
                }
            }
        }

        private long getCount() {
            for (; ; ) {
                long temp = this.count;
                if (compareAndGet(count, temp)) {
                    return temp;
                }
            }
        }

        private synchronized boolean compareAndGet(long v, long e) {
            if (v == e) {
                return true;
            }
            return false;
        }

        private synchronized boolean compareAndSwap(long v, long e, long n) {
            if (v == e) {
                count = n;
                return true;
            }
            return false;
        }
    }

    @Test
    public void testCas() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);
        Instant start = Instant.now();
        CountDownLatch cd = new CountDownLatch(TASK_COUNT);

        Cas cas = new Cas();
        Runnable t = () -> {
            long v = cas.getCount();
            while (v < TARGET_COUNT) {
                v = cas.inc();
            }
            System.out.println("cas:" + Duration.between(start, Instant.now()).toMillis() + ",count=" + v);
            cd.countDown();
        };
        IntStream.range(0, MAX_THREADS).forEach(i -> {
            service.submit(t);
        });
        cd.await();
        service.shutdown();
    }
}
