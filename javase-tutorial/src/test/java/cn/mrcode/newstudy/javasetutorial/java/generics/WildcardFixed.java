package cn.mrcode.newstudy.javasetutorial.java.generics;

import java.util.List;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/19     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/19 14:38
 * @date 2017/12/19 14:38
 * @since 1.0.0
 */
public class WildcardFixed {
    void foo(List<?> i) {
        fooHelper(i);
    }


    //创建Helper方法，以便捕获通配符
//通过类型推断
    private <T> void fooHelper(List<T> l) {
        l.set(0, l.get(0));
    }
}
