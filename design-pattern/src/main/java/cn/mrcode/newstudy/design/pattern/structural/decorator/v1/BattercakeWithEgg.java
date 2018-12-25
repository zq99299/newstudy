package cn.mrcode.newstudy.design.pattern.structural.decorator.v1;

/**
 * 加鸡蛋的煎饼
 *
 * @author : zhuqiang
 * @date : 2018/12/24 15:41
 */
public class BattercakeWithEgg extends Battercake {
    @Override
    protected String getDesc() {
        return super.getDesc() + " 加一个鸡蛋";
    }

    @Override
    protected int getCost() {
        return super.getCost() + 1;
    }
}
