package cn.mrcode.newstudy.design.pattern.structural.facade;

/**
 * 积分礼物
 *
 * @author : zhuqiang
 * @date : 2018/12/24 9:47
 */
public class PointsGift {
    private String name;

    public PointsGift(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PointsGift{" +
                "name='" + name + '\'' +
                '}';
    }
}
