package cn.mrcode.newstudy.design.pattern.creational.abstract_factory;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/29 22:50
 */
public class Test {
    public static void main(String[] args) {
        // 通过 java 产品族抽象工厂获取的产品一定都是 java 的产品
        CourseFactory factory = new JavaCourseFactory();
        Video video = factory.getVideo();
        Article article = factory.getArticle();
        video.produce();
        article.produce();
    }
}
