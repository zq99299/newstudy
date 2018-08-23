package cn.mrcode.newstudy.design.pattern.principle.openclose;

/**
 * java 类课程;还有js，算法等课程，需要分开
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/23 23:40
 */
public class JavaCourse implements ICourse {
    private Integer id;
    private String name;
    private Double price;

    public JavaCourse(Integer id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Double getPrice() {
        return this.price;
    }
}
