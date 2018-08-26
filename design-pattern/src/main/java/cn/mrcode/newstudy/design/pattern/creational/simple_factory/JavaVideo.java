package cn.mrcode.newstudy.design.pattern.creational.simple_factory;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 22:34
 */
public class JavaVideo extends Video {
    @Override
    public void produce() {
        System.out.println("录制 Java 课程");
    }
}
