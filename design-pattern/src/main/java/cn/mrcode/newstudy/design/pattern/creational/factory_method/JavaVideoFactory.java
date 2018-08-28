package cn.mrcode.newstudy.design.pattern.creational.factory_method;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/28 23:16
 */
public class JavaVideoFactory extends VideoFactory {
    @Override
    Video getVideo() {
        return new JavaVideo();
    }
}
