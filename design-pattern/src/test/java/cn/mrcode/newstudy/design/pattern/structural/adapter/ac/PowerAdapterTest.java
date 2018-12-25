package cn.mrcode.newstudy.design.pattern.structural.adapter.ac;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/25 21:31
 */
public class PowerAdapterTest {
    @Test
    public void fun1() {
        DC5 dc5 = new PowerAdapter();
        dc5.outputDC5V();
    }
}