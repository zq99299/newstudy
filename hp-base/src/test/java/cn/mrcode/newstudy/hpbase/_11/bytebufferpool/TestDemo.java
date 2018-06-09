package cn.mrcode.newstudy.hpbase._11.bytebufferpool;

import java.nio.ByteBuffer;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/9 20:24
 */
public class TestDemo {
    public static void main(String[] args) {
        ByteBufferPool pool = new ByteBufferPool();

        for (int i = 0; i < 100000; i++) {
            ByteBuffer buffer = pool.get(ThreadLocalRandom.current().nextInt(1000));
//            System.gc();
            if (i % 5 == 0) {
                // 模拟5次 退还一个
                pool.relese(buffer);
            }
        }
    }
}
