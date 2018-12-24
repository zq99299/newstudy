package cn.mrcode.newstudy.design.pattern.structural.facade;

/**
 * 积分支付服务
 *
 * @author : zhuqiang
 * @date : 2018/12/24 9:48
 */
public class PointsPaymentService {
    public boolean pay(PointsGift pointsGift) {
        System.out.println(pointsGift + " 支付扣减积分成功");
        return true;
    }
}
