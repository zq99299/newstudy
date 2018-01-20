package cn.mrcode.newstudy.temptest;

import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2017/12/25 16:23
 */
public class Storage {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private int maxSize = 2;  //仓库最大容量
    private LinkedList<Date> st = new LinkedList<Date>();  //仓库存储

    /** 生产 */
    public void producer() {
        lock.writeLock().lock();
        try {
            if (st.size() >= maxSize) {
//                System.out.println(Thread.currentThread().getName() + " : 仓库满了，先回去休息吧,库存 " + st.size());
                return;
            }
            st.push(new Date());
            System.out.println(Thread.currentThread().getName() + " : 生产了一个商品" + " 剩余库存：" + st.size());
        } finally {
            lock.writeLock().unlock();
        }

    }

    /** 消费 */
    public void consumer() {
        lock.readLock().lock();
        try {
            if (st.size() == 0) {
//                System.out.println(Thread.currentThread().getName() + " : 正在等待商品：当前商品数量：" + st.size());
                return;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : 消费商品：" + st.pop() + " 剩余库存：" + st.size());
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public LinkedList<Date> getSt() {
        return st;
    }

    public void setSt(LinkedList<Date> st) {
        this.st = st;
    }
}
