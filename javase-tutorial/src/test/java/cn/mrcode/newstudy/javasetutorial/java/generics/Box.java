package cn.mrcode.newstudy.javasetutorial.java.generics;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/15     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/15 15:28
 * @date 2017/12/15 15:28
 * @since 1.0.0
 */
public class Box<T> {

    private T t; // T stands for "Type"

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }
}