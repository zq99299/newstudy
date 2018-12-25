package cn.mrcode.newstudy.design.pattern.structural.flyweight;

import org.junit.Test;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/25 23:05
 */
public class EmployeeFactoryTest {
    // 假设有 4 个部门
    private String[] departments = {"RD", "QA", "BI", "DB"};

    @Test
    public void fun1() {
        // 总共做十次汇报，每次随机叫一个经理做
        // 这里的内容都是一样的
        EmployeeFactory factory = new EmployeeFactory();
        for (int i = 0; i < 10; i++) {
            int index = (int) (Math.random() * departments.length);
            factory.getManger(departments[index]).report();
        }
    }
}