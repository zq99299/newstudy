package cn.mrcode.newstudy.design.pattern.creational.abstract_factory;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/29 22:49
 */
public class JavaCourseFactory extends CourseFactory {
    @Override
    Video getVideo() {
        return new JavaVideo();
    }

    @Override
    Article getArticle() {
        return new JavaArticle();
    }
}
