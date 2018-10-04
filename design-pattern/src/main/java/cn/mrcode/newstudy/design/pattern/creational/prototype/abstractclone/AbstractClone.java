package cn.mrcode.newstudy.design.pattern.creational.prototype.abstractclone;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/10/4 23:14
 */
public abstract class AbstractClone implements Cloneable {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
