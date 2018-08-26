package cn.mrcode.newstudy.design.pattern.creational.simple_factory;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 22:39
 */
public class VideoFactory {
//    public Video getVideo(String type) {
//        if ("java".equalsIgnoreCase(type)) {
//            return new JavaVideo();
//        } else if ("python".equalsIgnoreCase(type)) {
//            return new PythonVideo();
//        }
//        return null;
//    }

    public Video getVideo(Class c) {
        String name = c.getName();
        Video video = null;
        try {
            video = (Video) Class.forName(name).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return video;
    }
}
