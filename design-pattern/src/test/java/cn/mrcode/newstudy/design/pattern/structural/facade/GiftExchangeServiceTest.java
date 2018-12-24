package cn.mrcode.newstudy.design.pattern.structural.facade;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/24 10:13
 */
public class GiftExchangeServiceTest {

    @Test
    public void giftExchange() {
        GiftExchangeService giftExchangeService = new GiftExchangeService();
        giftExchangeService.giftExchange(new PointsGift("iphone8"));
    }
}