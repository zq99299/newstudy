package cn.mrcode.newstudy.hpbase._09.coreserver;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/30 20:27
 */
public enum Method {
    GET,
    POST;

    public static Method lookup(String method) {
        String s = method.trim().toUpperCase();
        try {
            return valueOf(s);
        } catch (Exception e) {
            throw new IllegalArgumentException("不支持的方法 " + method);
        }
    }

    public static void main(String[] args) {
        Method method = Method.lookup("post");
        System.out.println(method);
    }
}
