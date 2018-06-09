package cn.mrcode.newstudy.hpbase._11.bytebufferpool;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

/**
 * 弱引用
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/9 20:01
 */
public class WeakByteBuffer extends WeakReference<ByteBuffer> {
    private final String name;
    private int capacity;
    private boolean idle;  // 是否空闲

    public int getCapacity() {
        return capacity;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    public String getName() {
        return name;
    }

    public WeakByteBuffer(ByteBuffer byteBuffer, ReferenceQueue<ByteBuffer> queue, int capacity, int size) {
        super(byteBuffer, queue);
        this.capacity = capacity;
        this.name = String.format("buff_%d_%d", capacity, size);
    }
}
