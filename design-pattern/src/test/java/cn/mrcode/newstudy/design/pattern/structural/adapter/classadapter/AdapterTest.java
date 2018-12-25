package cn.mrcode.newstudy.design.pattern.structural.adapter.classadapter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/25 20:44
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