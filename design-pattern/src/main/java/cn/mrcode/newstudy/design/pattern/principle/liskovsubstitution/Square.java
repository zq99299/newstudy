package cn.mrcode.newstudy.design.pattern.principle.liskovsubstitution;

/**
 * 正方形
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 16:19
 */

public class Square implements Quadrangle {
    // 边长：正方形的长宽是一样的
    private long sideLength;

    public long getSideLength() {
        return sideLength;
    }

    public void setSideLength(long sideLength) {
        this.sideLength = sideLength;
    }

    @Override
    public long getLength() {
        return getSideLength();
    }

    @Override
    public long getWidth() {
        return getSideLength();
    }
}
