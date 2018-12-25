package cn.mrcode.newstudy.design.pattern.structural.adapter.objectadapter;

/**
 * 具体的实现目标
 *
 * @author : zhuqiang
 * @date : 2018/12/25 20:35
 */
public class ConcreteTarget implements Target {
    @Override
    public void request() {
        System.out.println("目标方法");
    }
}
