package cn.mrcode.newstudy.temptest.hp_base;

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
public class VolatileMyCounter {
    private volatile boolean flag = false;
    private int value;

    public void incr() {
        value++;
    }

    //得到最后结果
    public int getCurValue() {
        return value;
    }
}
