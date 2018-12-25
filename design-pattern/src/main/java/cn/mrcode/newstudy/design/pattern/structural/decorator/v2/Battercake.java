package cn.mrcode.newstudy.design.pattern.structural.decorator.v2;

/**
 * 煎饼
 *
 * @author : zhuqiang
 * @date : 2018/12/24 15:39
 */
public class Battercake extends ABattercake {
    @Override
    protected String getDesc() {
        return "煎饼";
    }

    @Override
    protected int getCost() {
        return 8;
    }
}
