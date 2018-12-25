package cn.mrcode.newstudy.design.pattern.structural.adapter.objectadapter;

import org.junit.Test;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/25 20:54
 */
public class AdapterTest {
    @Test
    public void fun1() {
        Target target = new ConcreteTarget();
        target.request();

        Adapter adapter = new Adapter();
        adapter.request();
    }
}