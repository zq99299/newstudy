package cn.mrcode.newstudy.design.pattern.principle.singleresponsibility;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 13:27
 */
public class CourseImpl implements ICourseContent, ICourseManager {
    @Override
    public String getCourseName() {
        return null;
    }

    @Override
    public byte[] getCourseVideo() {
        return new byte[0];
    }

    @Override
    public void studyCourrse() {

    }

    @Override
    public void refundCourse() {

    }
}
