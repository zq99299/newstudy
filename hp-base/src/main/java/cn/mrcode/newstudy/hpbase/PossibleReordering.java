package cn.mrcode.newstudy.hpbase;

import java.util.stream.IntStream;

/**
 * CPU乱序验证测试
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2017/12/20 22:48
 */
public class PossibleReordering {
    int x = 0, y = 0;
    int a = 0, b = 0;
    volatile int vv = 0;

    public static void doTest() throws InterruptedException {
        PossibleReordering ordering = new PossibleReordering();
        Thread one = new Thread(() -> {
            ordering.a = 1;
            ordering.x = ordering.b;
        });

        Thread other = new Thread(() -> {
            ordering.b = 1;
            ordering.y = ordering.a;
        });

        one.start();
        other.start();

        one.join();
        other.join();

        System.out.println("run case:(" + ordering.x + "," + ordering.y + ")");
    }

    public static void main(String[] args) throws InterruptedException {
        IntStream.range(0, 100).forEach((i) -> {
            try {
                doTest();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
