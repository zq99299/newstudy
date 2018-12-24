package cn.mrcode.newstudy.design.pattern.structural.facade;

/**
 * 资格校验：积分校验，库存校验
 *
 * @author : zhuqiang
 * @date : 2018/12/24 9:49
 */
public class QualifyService {
    public boolean isAvailable(PointsGift pointsGift) {
        System.out.println("校验" + pointsGift + " 积分通过，库存通过");
        return true;
    }
}
