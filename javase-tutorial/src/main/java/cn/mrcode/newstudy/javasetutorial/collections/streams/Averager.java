package cn.mrcode.newstudy.javasetutorial.collections.streams;

import java.util.function.IntConsumer;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/08     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/8 13:57
 * @date 2017/12/8 13:57
 * @since 1.0.0
 */
public class Averager implements IntConsumer {
    private int total = 0;
    private int count = 0;

    public double average() {
        return count > 0 ? ((double) total) / count : 0;
    }

    @Override
    public void accept(int i) {
        total += i;
        count++;
        System.out.println(i);
    }

    public void combine(Averager other) {
        total += other.total;
        count += other.count;
        System.out.println("");
    }
}
