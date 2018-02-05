package cn.mrcode.newstudy.javasetutorial.java.generics;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/20     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/20 17:23
 * @date 2017/12/20 17:23
 * @since 1.0.0
 */
public class Singleton<T> {
    static class Shape { /* ... */ }
    static class Circle extends Shape { /* ... */ }
    static class Rectangle extends Shape { /* ... */ }

    static class Node<T> { /* ... */ }

    public static void main(String[] args) {
//        Node<Circle> nc = new Node<>();
//        Node<Shape>  ns = nc;
    }
}
