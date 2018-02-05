package cn.mrcode.newstudy.javasetutorial.data;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/14     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/14 16:11
 * @date 2017/12/14 16:11
 * @since 1.0.0
 */
public class Unboxing {
    public static void main(String[] args) {
        Integer i = new Integer(-8);

        // 1. 通过方法调用拆箱
        int absVal = absoluteValue(i);
        System.out.println("absolute value of " + i + " = " + absVal);

        List<Double> ld = new ArrayList<>();
        ld.add(3.1416);    // 是通过方法调用自动装箱的。

        // 2. 变量赋值拆箱
        double pi = ld.get(0);
        System.out.println("pi = " + pi);
    }

    public static int absoluteValue(int i) {
        return (i < 0) ? -i : i;
    }
}
