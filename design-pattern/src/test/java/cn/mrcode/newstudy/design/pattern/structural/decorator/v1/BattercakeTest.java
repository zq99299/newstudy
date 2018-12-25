package cn.mrcode.newstudy.design.pattern.structural.decorator.v1;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/24 15:44
 */
public class BattercakeTest {
    @Test
    public void fun1() {
        Battercake battercake = new Battercake();
        System.out.println(battercake.getDesc() + " 花费 " + battercake.getCost());

        BattercakeWithEgg battercakeWithEgg = new BattercakeWithEgg();
        System.out.println(battercakeWithEgg.getDesc() + " 花费 " + battercakeWithEgg.getCost());

        BattercakeWithEggSausage battercakeWithEggSausage = new BattercakeWithEggSausage();
        System.out.println(battercakeWithEggSausage.getDesc() + " 花费 " + battercakeWithEggSausage.getCost());
    }
}