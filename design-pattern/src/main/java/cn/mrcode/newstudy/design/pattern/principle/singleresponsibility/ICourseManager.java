package cn.mrcode.newstudy.design.pattern.principle.singleresponsibility;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 13:22
 */
public interface ICourseManager {
    // ~-----------  课程管理
    /** 学习课程 */
    void studyCourrse();
    /** 退款这门课程 */
    void refundCourse();
}
