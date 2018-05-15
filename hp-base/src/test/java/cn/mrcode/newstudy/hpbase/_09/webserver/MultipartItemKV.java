package cn.mrcode.newstudy.hpbase._09.webserver;

/**
 * 普通参数
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/14 23:45
 */
public class MultipartItemKV extends MultipartItem {
    private String value;

    public MultipartItemKV(String name) {
        super(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
