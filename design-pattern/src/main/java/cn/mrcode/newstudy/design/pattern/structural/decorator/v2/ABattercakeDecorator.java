package cn.mrcode.newstudy.design.pattern.structural.decorator.v2;

/**
 * 装饰者抽象类
 *
 * @author : zhuqiang
 * @date : 2018/12/24 15:51
 */
public class ABattercakeDecorator extends ABattercake {
    protected ABattercake battercake;

    // 通过构造传入一个抽象煎饼
    public ABattercakeDecorator(ABattercake battercake) {
        this.battercake = battercake;
    }

    @Override
    protected String getDesc() {
        return this.battercake.getDesc();
    }

    @Override
    protected int getCost() {
        return this.battercake.getCost();
    }
}
