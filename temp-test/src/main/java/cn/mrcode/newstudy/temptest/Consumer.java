package cn.mrcode.newstudy.temptest;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2017/12/25 16:25
 */
public class Consumer implements Runnable {
    private Storage st;

    public Consumer(Storage st) {
        this.st = st;
    }

    @Override
    public void run() {
        //这里不能使用循环多少次来模拟，不然会出现（假死锁），
        // 假如生产者的循环次数先循环完，那么消费者的循环次数还没有循环完，而又没有商品了，那么消费者则一直等待。没有人唤醒
        /*for (int i = 1;i <=50;i++)*/
        while (true) {
            st.consumer();
        }
    }
}
