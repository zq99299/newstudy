package cn.mrcode.newstudy.design.pattern.structural.decorator.v1;

/**
 * 加鸡蛋和香肠的煎饼
 *
 * @author : zhuqiang
 * @date : 2018/12/24 15:43
 */
public class BattercakeWithEggSausage extends BattercakeWithEgg {
    @Override
    protected String getDesc() {
        return super.getDesc() + " 加一根香肠";
    }

    @Override
    protected int getCost() {
        return super.getCost() + 2;
    }
}
