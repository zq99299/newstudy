package cn.mrcode.newstudy.javasetutorial.java.generics;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/19     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/19 15:34
 * @date 2017/12/19 15:34
 * @since 1.0.0
 */
public class Node<T extends Comparable<T>> {

    public T data;

    public Node(T data) {
        this.data = data;
    }

    public void setData(T data) {
        System.out.println("Node.setData");
        this.data = data;
    }
}
