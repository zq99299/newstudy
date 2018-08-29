package cn.mrcode.newstudy.design.pattern.creational.abstract_factory;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 22:35
 */
public class PythonVideo extends Video {
    @Override
    public void produce() {
        System.out.println("录制 Python 课程");
    }
}
