package cn.mrcode.newstudy.design.pattern.creational.abstract_factory;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/29 22:48
 */
public abstract class CourseFactory {
    abstract Video getVideo();

    abstract Article getArticle();
}
