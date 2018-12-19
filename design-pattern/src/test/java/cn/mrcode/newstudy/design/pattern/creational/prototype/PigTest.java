package cn.mrcode.newstudy.design.pattern.creational.prototype;

import org.junit.Test;

import java.util.Date;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/19 21:47
 */
public class PigTest {
    @Test
    public void fun1() throws CloneNotSupportedException, InterruptedException {
        Pig p1 = new Pig("小猪1", new Date());
        Pig p2 = (Pig) p1.clone();
        System.out.println(p1);
        System.out.println(p2);
        // 修改其中一个时间
        p2.getBirthday().setTime(6666666L);
        System.out.println(p1);
        System.out.println(p2);
    }

}