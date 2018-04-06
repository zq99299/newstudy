package cn.mrcode.newstudy.hpbase._05.q03;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/6 12:04
 */
public class LongAdderCounter implements Counter {
    private LongAdder value = new LongAdder();

    @Override
    public void incr() {
        value.increment();
    }

    @Override
    public long getCurValue() {
        return value.sum();
    }

    @Override
    public String toString() {
        return "LongAdderCounter";
    }
}
