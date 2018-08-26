package cn.mrcode.newstudy.design.pattern.principle.dependencyinversion;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 10:24
 */
public class Test {
    public static void main(String[] args) {
        // v1 不使用依赖倒置原则
//        Geely geely = new Geely();
//        geely.studyJavaCourse();
//        geely.studyFECourse();
//        geely.studyPythonCourse();

        Geely geely = new Geely();
        geely.studyImoocCourse(new JavaCourse());
        geely.studyImoocCourse(new FECourse());
    }
}
