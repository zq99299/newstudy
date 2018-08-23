package cn.mrcode.newstudy.design.pattern.principle.openclose;

/**
 * java课程打折
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/24 0:13
 */
public class JavaDiscountCourse extends JavaCourse {

    public JavaDiscountCourse(Integer id, String name, Double price) {
        super(id, name, price);
    }

    @Override
    public Double getPrice() {
        // 这里可以针对业务进行范围的判定等操作
        return super.getPrice() * 0.8;
    }

    /** 获取原价 */
    public Double getOriginPrice() {
        return super.getPrice();
    }
}
