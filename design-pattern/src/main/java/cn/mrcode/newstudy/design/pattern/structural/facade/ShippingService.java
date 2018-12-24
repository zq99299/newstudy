package cn.mrcode.newstudy.design.pattern.structural.facade;

/**
 * 物流服务
 *
 * @author : zhuqiang
 * @date : 2018/12/24 9:52
 */
public class ShippingService {
    public String shipGift(PointsGift pointsGift) {
        // 物流系统对接逻辑
        System.out.println(pointsGift + " 进入物流系统");
        String shippingOrderNo = "6666";
        return shippingOrderNo;
    }
}
