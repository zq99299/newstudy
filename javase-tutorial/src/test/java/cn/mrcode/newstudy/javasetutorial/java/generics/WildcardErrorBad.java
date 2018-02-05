package cn.mrcode.newstudy.javasetutorial.java.generics;

import java.util.List;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/19     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/19 14:42
 * @date 2017/12/19 14:42
 * @since 1.0.0
 */
public class WildcardErrorBad {
    void swapFirst(List<? extends Number> l1, List<? extends Number> l2) {
        Number temp = l1.get(0);
//        l1.set(0, l2.get(0)); // expected a CAP#1 extends Number,
        // got a CAP#2 extends Number;
        // same bound, but different types
//        l2.set(0, temp);        // expected a CAP#1 extends Number,
        // got a Number
    }
}
