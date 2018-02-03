package cn.mrcode.newstudy.temptest;

import org.junit.Test;

/**
 * ${desc}
 * @author zhuqiang
 * @version 1.0.1 2017/12/22 11:36
 * @date 2017/12/22 11:36
 * @since 1.0
 */
public class TestTest {
    @Test
    public void fun1() throws InterruptedException {
        VolatileExample volatileExample = new VolatileExample();

        Thread t1 = new Thread(() -> {
            volatileExample.writer();
        });

        Thread t2 = new Thread(() -> {
            volatileExample.reader();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("main end");
    }

    @Test
    public void fun2() {
        char hex[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        int b = 0xf1;
        // b = 0xf1
        System.out.println("b = 0x" + hex[(b >> 4) & 0x0f] + hex[b & 0x0f]);
    }

    @Test
    public void fun3() {
        char hex[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        byte b = (byte) 0xf;
        StringBuffer sb = new StringBuffer("0x");
        for (int i = 1; i <= 2; i++) {
            int num = (2 - i) * 4;
            char t = hex[(b >> num) & 0x0f];
            sb.append(t);
        }
        System.out.println(sb);
    }
}