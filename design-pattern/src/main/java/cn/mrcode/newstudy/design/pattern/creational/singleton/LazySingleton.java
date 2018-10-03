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
    private static boolean flag = true;

    private LazySingleton() {
        if (flag) {
            // 构造只能被调用一次
            flag = false;
        } else {
            throw new IllegalStateException("单例模式不允许使用反射创建");
        }
    }

    public synchronized static LazySingleton getInstance() {
        if (lazySingleton == null) {
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }
}
