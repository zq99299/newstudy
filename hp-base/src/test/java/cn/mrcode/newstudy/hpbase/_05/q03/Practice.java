package cn.mrcode.newstudy.hpbase._05.q03;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <pre>
 *     编程验证normal var ,volaitle，synchronize,atomicLong ,LongAdder，这几种做法实现的计数器方法，在多线程情况下的性能，准确度
 *
 *   class MyCounter
 *  {
 *      private long value;//根据需要进行替换
 *      public void incr();
 *       public long getCurValue();//得到最后结果
 *  }
 * 启动10个线程一起执行，每个线程调用incr() 100万次，
 * 所有线程结束后，打印 getCurValue()的结果，分析程序的结果 并作出解释。 用Stream和函数式编程实现则加分！
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/6 11:12
 */
public class Practice {
    @Test
    public void main() throws InterruptedException {
        test(new NormalCounter());
        test(new VolatileCounter());
        test(new SynchronizeCounter());
        test(new AtomicCounter());
        test(new LongAdderCounter());
    }

    public void test(Counter counter) throws InterruptedException {
        Instant start = Instant.now();
        List<Thread> ts = Stream.of(new Integer[10])
                .map(n -> {
                    Thread t = new Thread(() -> {
                        IntStream.range(0, 100_0000)
                                .forEach(i -> counter.incr());
                    });
                    t.start();
                    return t;
                })
                .collect(Collectors.toList());  // 终端操作才能触发中间操作的执行
        ts.forEach(t -> {
            try {
                // 会让当前线程等待t的结束
                // 这里十个线程，只有前一个结束了，后面的join才会被执行
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long total = Duration.between(start, Instant.now()).toMillis();
        System.out.println(counter + ":" + counter.getCurValue() + " ; 耗时:" + total);
    }
}
