package cn.mrcode.newstudy.design.pattern.principle.singleresponsibility;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 13:28
 */
public interface ICourseContent {
    // ~-----------  课程信息
    String getCourseName();

    byte[] getCourseVideo();
}
