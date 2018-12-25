package cn.mrcode.newstudy.design.pattern.structural.decorator.v2;

/**
 * 加鸡蛋的装饰器
 *
 * @author : zhuqiang
 * @date : 2018/12/24 15:56
 */
public class EggDecorator extends ABattercakeDecorator {
    public EggDecorator(ABattercake battercake) {
        super(battercake);
    }

    @Override
    protected String getDesc() {
        return super.getDesc() + " 加一个鸡蛋";
    }

    @Override
    protected int getCost() {
        return super.getCost() + 1;
    }
}
