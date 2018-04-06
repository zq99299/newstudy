package cn.mrcode.newstudy.hpbase._05.q03;

/**
 * 普通变量自增操作
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/6 10:51
 */
public class NormalCounter implements Counter {
    private long value;

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
        return "NormalCounter";
    }
}
