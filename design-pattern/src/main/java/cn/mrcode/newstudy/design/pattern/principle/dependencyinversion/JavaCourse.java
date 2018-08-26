package cn.mrcode.newstudy.design.pattern.principle.dependencyinversion;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 10:31
 */
public class JavaCourse implements ICourse {
    @Override
    public void studyCourse() {
        System.out.println("Geely 在学习 Java 课程");
    }
}
