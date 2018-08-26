package cn.mrcode.newstudy.design.pattern.principle.liskovsubstitution;

/**
 * 长方形
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 16:18
 */
public class Rectangle implements Quadrangle {
    private long length;
    private long width;

    public void setLength(long length) {
        this.length = length;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public long getWidth() {
        return 0;
    }
}
