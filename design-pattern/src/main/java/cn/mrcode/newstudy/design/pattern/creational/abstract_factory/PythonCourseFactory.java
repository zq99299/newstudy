package cn.mrcode.newstudy.design.pattern.creational.abstract_factory;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/29 22:49
 */
public class PythonCourseFactory extends CourseFactory {
    @Override
    Video getVideo() {
        return new PythonVideo();
    }

    @Override
    Article getArticle() {
        return new PythonArticle();
    }
}
