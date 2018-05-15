package cn.mrcode.newstudy.hpbase._09.webserver;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/14 23:43
 */
public abstract class MultipartItem {
    protected String name;

    public MultipartItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
