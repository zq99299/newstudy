package cn.mrcode.newstudy.design.pattern.creational.prototype.abstractclone;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/10/4 23:14
 */
public class A extends AbstractClone {
    public static void main(String[] args) throws CloneNotSupportedException {
        A a = new A();
        A aTempl = (A) a.clone();
    }
}
