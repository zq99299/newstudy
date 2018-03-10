package cn.mrcode.newstudy.hpbase._02.q05;

/**
 * 裁剪或填满，最多10字符，不足部分用 ! 填充
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/10 21:32
 */
public class CutOrFillStringHandlerDecorator extends FilterStringHandlerDecorator {
    public CutOrFillStringHandlerDecorator(StringHandlerComponent component) {
        super(component);
    }

    @Override
    public String hander(String str) {
        String hander = super.hander(str);
        int length = hander.length();
        if (length > 10) {
            return hander.substring(0, 10);
        }

        if (length < 10) {
            int fillSize = 10 - length;
            for (int i = 0; i < fillSize; i++) {
                hander += "!";
            }
        }
        return hander;
    }
}
