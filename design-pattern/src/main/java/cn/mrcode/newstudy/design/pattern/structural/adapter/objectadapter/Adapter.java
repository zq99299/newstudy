package cn.mrcode.newstudy.design.pattern.structural.adapter.objectadapter;

/**
 * 适配器
 *
 * @author : zhuqiang
 * @date : 2018/12/25 20:44
 */
public class Adapter implements Target {
    private Adaptee adaptee = new Adaptee();

    @Override
    public void request() {
        adaptee.adapteeRequest();
    }
}
