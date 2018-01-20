package cn.mrcode.newstudy.temptest;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2017/12/25 16:24
 */
public class Producer implements Runnable {
    private Storage st;

    public Producer(Storage st) {
        this.st = st;
    }

    @Override
    public void run() {
        /*for (int i = 1;i <=50;i++)*/
        while (true) {
            st.producer();
        }

    }
}
