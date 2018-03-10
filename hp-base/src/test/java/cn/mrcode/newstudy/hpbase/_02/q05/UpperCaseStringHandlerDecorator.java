package cn.mrcode.newstudy.hpbase._02.q05;

/**
 * 转大写
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/10 21:32
 */
public class UpperCaseStringHandlerDecorator extends FilterStringHandlerDecorator {
    public UpperCaseStringHandlerDecorator(StringHandlerComponent component) {
        super(component);
    }

    @Override
    public String hander(String str) {
        String hander = super.hander(str);
        return hander.toUpperCase();
    }
}
