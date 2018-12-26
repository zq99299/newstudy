package cn.mrcode.newstudy.design.pattern.structural.composite;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/26 21:57
 */
public class CourseCatalogTest {
    @Test
    public void fun1() {
        Course linux = new Course("Linux系统", 90);
        Course windows = new Course("Windows系统", 80);

        CourseCatalog javaCourseCatalog = new CourseCatalog("Java课程目录", 2);
        Course c1 = new Course("实战java虚拟机", 22);
        Course c2 = new Course("Spring技术内幕", 33);
        Course c3 = new Course("深入分析java虚拟机", 44);
        javaCourseCatalog.add(c1);
        javaCourseCatalog.add(c2);
        javaCourseCatalog.add(c3);

        CourseCatalog imoocCourseCatalog = new CourseCatalog("慕课网课程目录", 1);
        imoocCourseCatalog.add(linux);
        imoocCourseCatalog.add(windows);
        imoocCourseCatalog.add(javaCourseCatalog);
        imoocCourseCatalog.print();
    }
}