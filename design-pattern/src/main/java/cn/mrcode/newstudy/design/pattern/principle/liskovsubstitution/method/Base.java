package cn.mrcode.newstudy.design.pattern.principle.liskovsubstitution.method;

import java.util.Map;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 16:59
 */
public class Base {
    public void method(Map hashMap) {
        System.out.println("父类被执行");
    }
}
