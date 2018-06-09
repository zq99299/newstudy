package cn.mrcode.newstudy.hpbase._11.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/9 19:40
 */
public class TestDemo {
    private static class WeakByteBuffer extends WeakReference<ByteBuffer> {

        private int id;

        public int getId() {
            return id;
        }

        public WeakByteBuffer(ByteBuffer referent, ReferenceQueue<ByteBuffer> queue, int id) {
            super(referent, queue);
            this.id = id;
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Map<Integer, WeakByteBuffer> referent = Collections.synchronizedMap(new HashMap<>());
        ReferenceQueue<ByteBuffer> queue = new ReferenceQueue<>();

        for (int i = 0; i < 60000; i++) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(i);
            referent.put(i, new WeakByteBuffer(buffer, queue, i));
            System.gc();
            WeakByteBuffer se = null;
            while ((se = (WeakByteBuffer) queue.poll()) != null) {
                referent.remove(se.id);
                System.out.println("对象ID : " + se.getId() + "已经被JVM回收");
            }
        }

    }
}
