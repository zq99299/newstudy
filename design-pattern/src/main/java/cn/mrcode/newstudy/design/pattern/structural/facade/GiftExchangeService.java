package cn.mrcode.newstudy.design.pattern.structural.facade;

/**
 * 外观模式服务
 *
 * @author : zhuqiang
 * @date : 2018/12/24 10:00
 */
public class GiftExchangeService {
    private QualifyService qualifyService;
    private PointsPaymentService pointsPaymentService;
    private ShippingService shippingService;

    public GiftExchangeService() {
        this.qualifyService = new QualifyService();
        this.pointsPaymentService = new PointsPaymentService();
        this.shippingService = new ShippingService();
    }

    /**
     * 礼物兑换
     */
    public void giftExchange(PointsGift pointsGift) {
        if (qualifyService.isAvailable(pointsGift)
                && pointsPaymentService.pay(pointsGift)) {
            String shippingOrderNo = shippingService.shipGift(pointsGift);
            System.out.println(pointsGift + " 返回的订单号 " + shippingOrderNo);
        }
    }
}
