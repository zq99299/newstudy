package cn.mrcode.newstudy.hpbase._11;

import org.junit.Test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/5 22:12
 */
public class TestDemo {
    @Test
    public void fun1() {
        /**
         * <pre>
         * 挑战1：
         * 使用 经典版的reactor模型实现TelnetServer;
         * reactor模型组成：
         *  acceptor 专门用来处理接受链接和初始化配置链接
         *  ioHandler 专门用来处理读写事件；
         *
         * 是一个单线程的处理版本；
         * 要实现的功能有：
         *      服务端：
         *          1. 接受并解析客户端发送过来的命令；客户端有可能发送多个命令；需要解析；
         *              要注意的是：接收一个命令就暂时取消读事件，等待写完数据后，再恢复读事件
         *          2. 根据相应的命令执行操作；非特定命令就发送一大串字符给客户端；
         *              数据一次未写完则下次继续；写完则恢复读事件，取消写事件
         *      客户端Tennet：
         *          1. 输入一个发送一个字符，以回车结尾；可以输入多个字符然后主动回车
         *          2. 打印服务端的数据
         *
         * 挑战2：
         *</pre>
         *
         */
//        ByteBuffer.allocate(5)
    }

    @Test
    public void fun2() throws InterruptedException {
        ReferenceQueue<byte[]> referenceQueue = new ReferenceQueue<>();
        Object value = new Object();
        Map<Object, Object> map = new HashMap<>();

        for (int i = 0; i < 10000; i++) {

            byte[] bytes = new byte[1 * 1024 * 1024];

            WeakReference<byte[]> weakReference = new WeakReference<byte[]>(bytes, referenceQueue);
            map.put(weakReference, value);

        }
        System.out.println("map.size->" + map.size());
        Thread thread = new Thread(() -> {
            try {

                int cnt = 0;

                WeakReference<byte[]> k;

                while ((k = (WeakReference) referenceQueue.remove()) != null) {

                    System.out.println((cnt++) + "回收了:" + k);

                }

            } catch (InterruptedException e) {

                //结束循环

            }

        });

        thread.setDaemon(true);

        thread.start();
        thread.join();
    }

    @Test
    public void fun3() throws InterruptedException {
        ReferenceQueue<ByteBuffer> referenceQueue = new ReferenceQueue<>();
        Map<Object, Object> map = new HashMap<>();
        Thread thread = new Thread(() -> {
            int cnt = 0;
            try {
                WeakReference k = null;
                while ((k = (WeakReference) referenceQueue.remove()) != null) {
                    System.out.println((cnt++) + "回收了:" + k);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        for (int i = 0; i < 10000; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(10 * 10000);
            new WeakReference<>(buffer, referenceQueue);
            buffer = null;
        }
        thread.join();

    }
}

