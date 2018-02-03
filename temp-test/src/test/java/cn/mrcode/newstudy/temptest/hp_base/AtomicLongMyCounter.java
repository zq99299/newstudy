package cn.mrcode.newstudy.temptest.hp_base;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/22     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/22 14:44
 * @date 2017/12/22 14:44
 * @since 1.0.0
 */

// volatile + synchronize 实现
public class AtomicLongMyCounter {
    private AtomicLong value = new AtomicLong(0);//根据需要进行替换

    public void incr() {
        value.incrementAndGet();
    }

    //得到最后结果
    public int getCurValue() {
        return value.intValue();
    }
}
