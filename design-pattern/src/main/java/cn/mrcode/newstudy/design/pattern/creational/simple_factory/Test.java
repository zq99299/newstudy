package cn.mrcode.newstudy.design.pattern.creational.simple_factory;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 22:35
 */
public class Test {
    public static void main(String[] args) {
//        Video video = new JavaVideo();
//        video.produce();
//        VideoFactory factory = new VideoFactory();
//        Video video = factory.getVideo("java");
//        video.produce();//
        VideoFactory factory = new VideoFactory();
        Video video = factory.getVideo(JavaVideo.class);
        video.produce();
    }
}
