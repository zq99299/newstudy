package cn.mrcode.newstudy.design.pattern.structural.decorator.v2;

/**
 * 被装饰者抽象类 - 煎饼
 *
 * @author : zhuqiang
 * @date : 2018/12/24 15:49
 */
public abstract class ABattercake {
    protected abstract String getDesc();

    /**
     * 费用
     */
    protected abstract int getCost();
}
