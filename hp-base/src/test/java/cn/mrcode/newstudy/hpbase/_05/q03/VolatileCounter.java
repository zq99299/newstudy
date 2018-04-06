package cn.mrcode.newstudy.hpbase._05.q03;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/6 12:04
 */
public class VolatileCounter implements Counter {
    private volatile long value;

    @Override
    public void incr() {
        ++value;
    }

    @Override
    public long getCurValue() {
        return value;
    }
    @Override
    public String toString() {
        return "VolatileCounter";
    }
}
