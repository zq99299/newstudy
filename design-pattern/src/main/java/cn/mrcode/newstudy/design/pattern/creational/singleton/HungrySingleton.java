package cn.mrcode.newstudy.design.pattern.creational.singleton;

import java.io.Serializable;

/**
 * 恶汉式单例模式
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/9/28 22:08
 */
public class HungrySingleton implements Serializable {
    private final static HungrySingleton hungrySingleton = new HungrySingleton();

    private HungrySingleton() {
    }

    public static HungrySingleton getInstance() {
        return hungrySingleton;
    }

    private Object readResolve() {
        return hungrySingleton;
    }
}
