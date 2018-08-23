package cn.mrcode.newstudy.design.pattern.principle.openclose;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/23 23:55
 */
public class Test {
    public static void main(String[] args) {
        ICourse iCourse = new JavaDiscountCourse(96, "设计模式", 389d);
        JavaDiscountCourse javaCourse = (JavaDiscountCourse) iCourse;
        System.out.println("ID:" + javaCourse.getId() +
                "，课程名称：" + javaCourse.getName() +
                "，价格：" + javaCourse.getPrice() +
                ", 原价：" + javaCourse.getOriginPrice()
        );
    }
}
