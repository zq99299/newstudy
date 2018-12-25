package cn.mrcode.newstudy.design.pattern.structural.decorator.v2;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/24 15:58
 */
public class BattercakeTest {
    @Test
    public void fun2() {
        ABattercake battercake;
        battercake = new Battercake(); // 一个标准煎饼
        battercake = new EggDecorator(battercake); // 加一个鸡蛋的
        battercake = new EggDecorator(battercake); // 再加一个鸡蛋的
        battercake = new SausageDecorator(battercake); // 在加一根香肠的
        System.out.println(battercake.getDesc() + " 花费 " + battercake.getCost());
    }
}