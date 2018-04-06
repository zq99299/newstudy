package cn.mrcode.newstudy.hpbase._05.q03;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/6 12:04
 */
public class AtomicCounter implements Counter {
    private AtomicLong value = new AtomicLong();

    @Override
    public void incr() {
        value.incrementAndGet();
    }

    @Override
    public long getCurValue() {
        return value.get();
    }

    @Override
    public String toString() {
        return "AtomicCounter";
    }
}
