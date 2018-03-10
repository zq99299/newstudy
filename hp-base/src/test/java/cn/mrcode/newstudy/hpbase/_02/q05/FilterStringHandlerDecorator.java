package cn.mrcode.newstudy.hpbase._02.q05;

/**
 * 装饰器接口
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/10 21:18
 */
public abstract class FilterStringHandlerDecorator implements StringHandlerComponent {
    // 持有一个component
    private StringHandlerComponent component;

    public FilterStringHandlerDecorator(StringHandlerComponent component) {
        this.component = component;
    }

    @Override
    public String hander(String str) {
        return component.hander(str);
    }
}
