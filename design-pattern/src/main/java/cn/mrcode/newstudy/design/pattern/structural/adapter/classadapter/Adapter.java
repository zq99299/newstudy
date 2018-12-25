package cn.mrcode.newstudy.design.pattern.structural.adapter.classadapter;

/**
 * 适配器
 *
 * @author : zhuqiang
 * @date : 2018/12/25 20:44
 */
public class Adapter extends Adaptee implements Target {
    @Override
    public void request() {
        super.adapteeRequest();
    }
}
