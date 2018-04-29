package cn.mrcode.newstudy.hpbase._07;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/04/27     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/4/27 14:53
 * @date 2018/4/27 14:53
 * @since 1.0.0
 */
public class SynchronousQueueTest {
    /**
     * 2个线程往队列中写数据；一个线程从队列中获取数据，使用drainTo api 从队列中把数据转移到另一个容器中
     * @throws InterruptedException
     */
    @Test
    public void testDrainToN() throws InterruptedException {
        final SynchronousQueue q = new SynchronousQueue();
        Thread t1 = new Thread(() -> {
            try {
                q.put("one");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                q.put("two");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();


        ArrayList list = new ArrayList();
        int drained;
        while ((drained = q.drainTo(list, 1)) == 0) Thread.yield();
        assertEquals(1, list.size());
        while ((drained = q.drainTo(list, 1)) == 0) Thread.yield();
        assertEquals(1, drained);
        assertEquals(2, list.size());
        assertTrue(list.contains("one"));
        assertTrue(list.contains("two"));

        t1.join();
        t2.join();
    }

    // 排队策略的测试

    /**
     * 看到很多项目都用了该队列来生成线程池;
     * 使用了SynchronousQueue；每次加入队列失败就新建线程执行；所以相当于池没有作用？
     */
    @Test
    public void synchronousQueueExecutorTest() {
//        corePoolSize - 池中所保存的线程数，包括空闲线程。
//        maximumPoolSize - 池中允许的最大线程数。
//        keepAliveTime - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
//        unit - keepAliveTime 参数的时间单位。
//        workQueue - 执行前用于保持任务的队列。此队列仅保持由 execute 方法提交的 Runnable 任务。
        BlockingQueue<Runnable> workQueue = new SynchronousQueue<>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5,
                                                             60, TimeUnit.SECONDS,
                                                             workQueue);

        commExecutor(executor);
    }

    /**
     * workQueue : 用于排策略；ArrayBlockingQueue；这里可以存储30个；
     * 当超过 maximumPoolSize 的时候 会抛出异常；
     * <p>
     * 下面代码的效果，同时只能运行2个线程；最多接受5个线程，超过2个的线程则进入队列中排队等待
     */
    @Test
    public void arrayBlockingQueueExecutorTest() {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(30);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5,
                                                             60, TimeUnit.SECONDS,
                                                             workQueue);
        commExecutor(executor);
    }

    /**
     * 这里把ArrayBlockingQueue设置成1.也就是每次只会有一个线程进行等待；和 synchronousQueue类似，只不过这里表现出来的现象是：
     * 4个线程并行，最后一个被加入到队列中，直到有空余线程，再执行
     */
    @Test
    public void arrayBlockingQueueExecutorTest2() {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5,
                                                             60, TimeUnit.SECONDS,
                                                             workQueue);
        commExecutor(executor);
    }

    private void commExecutor(ThreadPoolExecutor executor) {
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
                    executor.execute(r);
                });

        try {
            executor.shutdown();
            executor.awaitTermination(1000, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
