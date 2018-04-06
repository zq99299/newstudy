package cn.mrcode.newstudy.hpbase._05.q03;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/6 12:04
 */
public class SynchronizeCounter implements Counter {
    private long value;

    @Override
    public synchronized void incr() {
        ++value;
    }

    @Override
    public synchronized long getCurValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SynchronizeCounter";
    }
}
