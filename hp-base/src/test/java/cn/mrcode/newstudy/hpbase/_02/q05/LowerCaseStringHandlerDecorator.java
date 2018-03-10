package cn.mrcode.newstudy.hpbase._02.q05;

/**
 * 转小写
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/10 21:32
 */
public class LowerCaseStringHandlerDecorator extends FilterStringHandlerDecorator {
    public LowerCaseStringHandlerDecorator(StringHandlerComponent component) {
        super(component);
    }

    @Override
    public String hander(String str) {
        String hander = super.hander(str);
        return hander.toLowerCase();
    }
}
