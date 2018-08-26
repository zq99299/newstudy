package cn.mrcode.newstudy.design.pattern.principle.liskovsubstitution.method;

import java.util.HashMap;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 17:00
 */
public class Child extends Base {
//    @Override
    public void method(HashMap hashMap) {
        System.out.println("子类 HashMap 被执行");
    }

    // 比父类入参更宽松，不是重写，是重载
//    public void method(Map map) {
//        System.out.println("子类 Map 被执行");
//    }
}
