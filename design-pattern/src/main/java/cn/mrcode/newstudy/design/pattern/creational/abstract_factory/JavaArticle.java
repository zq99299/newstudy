package cn.mrcode.newstudy.design.pattern.creational.abstract_factory;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/29 22:46
 */
public class JavaArticle extends Article{
    @Override
    public void produce() {
        System.out.println("编写 Java 手记");
    }
}
