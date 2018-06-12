package cn.mrcode.newstudy.hpbase._12.mycat;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * ${todo}
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/11 21:32
 */
public class DirectByteBufferPoolTest {
    @Test
    public void fun1() throws InterruptedException {
        DirectByteBufferPool pool = new DirectByteBufferPool(1024 * 4, (short) 512, (short) 1);
        System.out.println(pool.allocate(2048));
        ByteBuffer bb2 = pool.allocate(2048);
        System.out.println(bb2);
        pool.recycle(bb2);
        System.out.println(pool.allocate(2));
//        List<Thread> ts = IntStream.range(0, 2).mapToObj(i ->
//        {
//            Thread t = new Thread(() -> {
//                for (int j = 0; j < 5; j++) {
//                    int size = ThreadLocalRandom.current().nextInt(1024);
//                    ByteBuffer allocate = pool.allocate(size);
//                    System.out.println(String.format("allocate size %d,buffer = %s", size, allocate));
//                }
//            });
//            t.start();
//            return t;
//        })
//                .collect(Collectors.toList());
//        for (Thread t : ts) {
//            t.join();
//        }
    }
}