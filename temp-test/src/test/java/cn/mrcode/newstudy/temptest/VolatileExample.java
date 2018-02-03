package cn.mrcode.newstudy.temptest;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/22     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/22 11:37
 * @date 2017/12/22 11:37
 * @since 1.0.0
 */
public class VolatileExample {
    int a = 0;
    volatile boolean flag = false;

    public void writer() {
        a = 1;      // 1
        flag = true; // 2
    }

    public void reader() {
        while (!flag) {
            System.out.println("..");
        }

        int i = a;// 4
        System.out.println("-----------" + i);
    }
}
