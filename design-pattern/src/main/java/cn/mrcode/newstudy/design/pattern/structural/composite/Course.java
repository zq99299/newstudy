package cn.mrcode.newstudy.design.pattern.structural.composite;

/**
 * 课程
 *
 * @author : zhuqiang
 * @date : 2018/12/26 21:50
 */
public class Course extends CatalogComponent {
    private String name;
    private double price;

    public Course(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public void print() {
        System.out.println("CourseName：" + name + " Price：" + price);
    }
}
