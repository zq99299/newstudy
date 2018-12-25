package cn.mrcode.newstudy.design.pattern.structural.decorator.v1;

/**
 * 煎饼
 *
 * @author : zhuqiang
 * @date : 2018/12/24 15:39
 */
public class Battercake {
    protected String getDesc() {
        return "煎饼";
    }

    /** 费用 */
    protected int getCost() {
        return 8;
    }
}
