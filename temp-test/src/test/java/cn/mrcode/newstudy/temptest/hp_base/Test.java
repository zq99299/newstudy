package cn.mrcode.newstudy.temptest.hp_base;

import java.util.stream.IntStream;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/22     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/22 15:03
 * @date 2017/12/22 15:03
 * @since 1.0.0
 */
public class Test {
    @org.junit.Test
    public void myCounter() {
//        VolatileMyCounter counter = new VolatileMyCounter();
//        NormalMyCounter counter = new NormalMyCounter();
        LongAdderMyCounter counter = new LongAdderMyCounter();
//        AtomicLongMyCounter counter = new AtomicLongMyCounter();
        Thread[] ts = new Thread[10];
        long startTime = System.currentTimeMillis();
        IntStream.range(0, 10)
                .forEach(i -> {
                    ts[i] =
                            new Thread(() -> {
                                IntStream.range(0, 10_0000)
                                        .forEach(j -> {
                                            counter.incr();
                                        });
                            });
                    ts[i].start();
                });
        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(counter.getCurValue());
        System.out.println("耗时：" + (endTime - startTime));
    }
}
