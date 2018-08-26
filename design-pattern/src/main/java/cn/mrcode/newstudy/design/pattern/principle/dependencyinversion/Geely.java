package cn.mrcode.newstudy.design.pattern.principle.dependencyinversion;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 10:24
 */
public class Geely {
    public void studyImoocCourse(ICourse iCourse) {
        iCourse.studyCourse();
    }
}
