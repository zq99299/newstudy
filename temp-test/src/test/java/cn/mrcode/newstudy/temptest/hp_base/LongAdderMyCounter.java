package cn.mrcode.newstudy.temptest.hp_base;

import java.util.concurrent.atomic.LongAdder;

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
public class LongAdderMyCounter {
    private LongAdder value = new LongAdder();//根据需要进行替换

    public void incr() {
        value.increment();
    }

    //得到最后结果
    public int getCurValue() {
        return value.intValue();
    }
}
