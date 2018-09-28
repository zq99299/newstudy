package cn.mrcode.newstudy.design.pattern.creational.singleton;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/9/27 22:15
 */
public class TestDemo {
    public static void main(String[] args) {
//        LazySingleton instance = LazySingleton.getInstance();
//        System.out.println(instance);
        // 使用多线程来 演示 debug 怎么调试

//        new Thread(() -> LazySingleton.getInstance()).start();
//        new Thread(() -> LazySingleton.getInstance()).start();
        StaticInnerClassSingleton.print();
//        StaticInnerClassSingleton instance = StaticInnerClassSingleton.getInstance();
        System.out.println("done");
    }
}
