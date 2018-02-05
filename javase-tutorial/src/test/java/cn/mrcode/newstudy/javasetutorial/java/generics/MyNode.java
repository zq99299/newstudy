package cn.mrcode.newstudy.javasetutorial.java.generics;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/19     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/19 16:12
 * @date 2017/12/19 16:12
 * @since 1.0.0
 */
public class MyNode extends Node<Integer> {
    public MyNode(Integer data) {
        super(data);
    }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}