package cn.mrcode.newstudy.design.pattern.creational.singleton;

/**
 * 静态内部类懒汉式单例模式
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/9/27 23:21
 */
public class StaticInnerClassSingleton {
    {
        System.out.println("单例类初始化");
    }

    private static class InnerClass {
        static {
            System.out.println("内部类初始化");
        }

        private static StaticInnerClassSingleton instance = new StaticInnerClassSingleton();
    }

    public static void print() {
        System.out.println("测试是否是延迟初始化的");
    }

    public static StaticInnerClassSingleton getInstance() {
        return InnerClass.instance;
    }
}
