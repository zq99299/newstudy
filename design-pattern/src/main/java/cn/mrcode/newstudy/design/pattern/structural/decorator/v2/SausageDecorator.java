package cn.mrcode.newstudy.design.pattern.structural.decorator.v2;

/**
 * 加香肠的装饰器
 *
 * @author : zhuqiang
 * @date : 2018/12/24 15:57
 */
public class SausageDecorator extends ABattercakeDecorator {
    public SausageDecorator(ABattercake battercake) {
        super(battercake);
    }

    @Override
    protected String getDesc() {
        return super.getDesc() + " 加一根香肠";
    }

    @Override
    protected int getCost() {
        return super.getCost() + 2;
    }
}
