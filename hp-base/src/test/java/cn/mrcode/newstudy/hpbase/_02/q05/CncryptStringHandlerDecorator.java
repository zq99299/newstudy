package cn.mrcode.newstudy.hpbase._02.q05;

/**
 * 加密装饰
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/10 21:20
 */
public class CncryptStringHandlerDecorator extends FilterStringHandlerDecorator {
    public CncryptStringHandlerDecorator(StringHandlerComponent component) {
        super(component);
    }

    @Override
    public String hander(String str) {
        String hander = super.hander(str);
        // 加密
        byte[] bytes = hander.getBytes();
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = (byte) (bytes[i] - 1);
        }
        return new String(newBytes);
    }
}
