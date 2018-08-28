package cn.mrcode.newstudy.design.pattern.creational.factory_method;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 22:35
 */
public class Test {
    public static void main(String[] args) {
        VideoFactory factory = new JavaVideoFactory();
        Video video = factory.getVideo();
        video.produce();

//        LoggerFactory.getLogger()
    }
}
