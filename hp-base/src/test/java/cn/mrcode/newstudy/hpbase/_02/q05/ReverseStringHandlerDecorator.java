package cn.mrcode.newstudy.hpbase._02.q05;

/**
 * 反转装饰器
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/10 21:31
 */
public class ReverseStringHandlerDecorator extends FilterStringHandlerDecorator {
    public ReverseStringHandlerDecorator(StringHandlerComponent component) {
        super(component);
    }

    @Override
    public String hander(String str) {
        String hander = super.hander(str);
        return new StringBuilder(hander).reverse().toString();
    }
}
