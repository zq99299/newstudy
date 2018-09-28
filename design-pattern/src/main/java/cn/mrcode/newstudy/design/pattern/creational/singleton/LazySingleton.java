package cn.mrcode.newstudy.design.pattern.creational.singleton;

/**
 * 懒汉式 单例模式
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/9/27 22:06
 */
public class LazySingleton {
    /**
     * 1. 私有构造
     * 2. 内部持有一个实例
     */
    private static LazySingleton lazySingleton = null;

    private LazySingleton() {
    }

    public synchronized static LazySingleton getInstance() {
        if (lazySingleton == null) {
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }
}
