package cn.mrcode.newstudy.design.pattern.principle.liskovsubstitution.method;

import java.util.HashMap;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 17:02
 */
public class Test {
    public static void main(String[] args) {
        Child child = new Child();
        HashMap hashMap = new HashMap<>();
        child.method(hashMap);
    }
}
