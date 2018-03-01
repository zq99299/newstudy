package cn.mrcode.newstudy.hpbase._01;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/2/5 22:23
 */
public class Practice {
    @Test
    public void fun1() {
        Integer a = 100;
        Integer b = 100;
        Integer c = 200;
        Integer d = 200;
        System.out.println(a == b);
        System.out.println(c == d);
    }

    @Test
    public void _02() {
        int a = -1024;
        int b = a >> 1;
        int c = a >>> 1;
        // 11111111111111111111110000000000
        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toBinaryString(b));
        System.out.println(Integer.toBinaryString(c));
        System.out.println(b);
        System.out.println(c);
    }

    @Test
    public void _03() {
        int[][] arr = new int[5][5];
        for (int i = 0; i < arr.length; i++) {
            int length = arr[i].length;
            for (int j = 0; j < length; j++) {
                arr[i][j] = j;
            }
        }
        System.out.println("------------ 行优先 --------------");
        for (int i = 0; i < arr.length; i++) {
            int length = arr[i].length;
            for (int j = 0; j < length; j++) {
                if (j + 1 == length) {
                    System.out.print(arr[i][j]);
                } else {
                    System.out.print(arr[i][j] + " , ");
                }
            }
            System.out.println("");
        }

        System.out.println("------------ 列优先 --------------");
        for (int i = 0; i < arr.length; i++) {
            int length = arr[i].length;
            for (int j = 0; j < length; j++) {
                if (j + 1 == length) {
                    System.out.print(arr[j][i]);
                } else {
                    System.out.print(arr[j][i] + " , ");
                }
            }
            System.out.println("");
        }
    }

    @Test
    public void _03_01() {
        byte[][] arr = new byte[10240][10240];
        for (int i = 0; i < arr.length; i++) {
            int length = arr[i].length;
            for (int j = 0; j < length; j++) {
//                arr[i][j] = 1;
                arr[j][i] = 1;
            }
        }
        _03_01Row(arr);
        _03_01Column(arr);
    }

    private void _03_01Row(byte[][] arr) {
        Instant start = Instant.now();
        int total = 0;
        for (int i = 0; i < arr.length; i++) {
            int length = arr[i].length;
            for (int j = 0; j < length; j++) {
                total += arr[i][j];
            }
        }
        Instant end = Instant.now();
        System.out.println("结果" + total
                + ";耗时：" + Duration.between(start, end).toMillis());
    }

    private void _03_01Column(byte[][] arr) {
        Instant start = Instant.now();
        int total = 0;
        for (int i = 0; i < arr.length; i++) {
            int length = arr[i].length;
            for (int j = 0; j < length; j++) {
                total += arr[j][i];
            }
        }
        Instant end = Instant.now();
        System.out.println("结果" + total
                + ";耗时：" + Duration.between(start, end).toMillis());
    }
}
