package cn.mrcode.newstudy.hpbase._11.bytebufferpool;

import java.lang.ref.ReferenceQueue;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/9 19:59
 */
public class ByteBufferPool {
    // 空闲  每个容量可能有多个buffer
    private TreeMap<Integer, LinkedList<WeakByteBuffer>> pool = new TreeMap<>();
    // 空间不够用的时候 回收空闲buffer
    private ReferenceQueue<ByteBuffer> queue = new ReferenceQueue<>();

    public synchronized ByteBuffer get(int capacity) {
        cleanCache();
        Integer key = pool.higherKey(capacity);
        if (key != null) {
            LinkedList<WeakByteBuffer> buffers = pool.get(key);
            int size = buffers.size();
            int index = 0;
            while (index != size) {
                WeakByteBuffer buffer = buffers.poll();
                if (buffer.isIdle()) {  // 检查是否空闲
                    buffer.setIdle(false);
                    buffers.offer(buffer);  // 把占用的放入栈底
                    System.out.println(String.format("hit %s", buffer.getName()));
                    return buffer.get();
                }
                buffers.offer(buffer);
                index++;
            }
        }

        // 没有则新建
        LinkedList<WeakByteBuffer> buffers = pool.get(capacity);
        if (buffers == null) {
            buffers = new LinkedList<>();
            pool.put(capacity, buffers);
        }
        WeakByteBuffer buffer = new WeakByteBuffer(ByteBuffer.allocate(capacity), queue, capacity, buffers.size());
        buffers.offer(buffer);
        return buffer.get();
    }

    // 归还释放对该buffer的使用
    public synchronized void relese(ByteBuffer byteBuffer) {
        int capacity = byteBuffer.capacity();
        LinkedList<WeakByteBuffer> buffers = pool.get(capacity);
        if (buffers == null) {
            return;
        }

        WeakByteBuffer buffer = find(buffers, byteBuffer);
        if (byteBuffer == null) return;  // 不接受非本pool创建的buffer
        buffer.setIdle(true);
        byteBuffer.clear();
        // 把空闲的放入栈顶
        buffers.push(buffer);
    }

    public WeakByteBuffer find(LinkedList<WeakByteBuffer> buffers, ByteBuffer byteBuffer) {
        Iterator<WeakByteBuffer> it = buffers.iterator();
        WeakByteBuffer buffer = null;
        while (it.hasNext()) {
            buffer = it.next();
            if (buffer.get() == byteBuffer) {
                it.remove();
                break;
            }
        }
        return buffer;
    }

    private void cleanCache() {
        WeakByteBuffer wbb = null;
        while ((wbb = (WeakByteBuffer) queue.poll()) != null) {
            int capacity = wbb.getCapacity();
            LinkedList<WeakByteBuffer> buffers = pool.get(capacity);
            Iterator<WeakByteBuffer> it = buffers.iterator();
            while (it.hasNext()) {
                WeakByteBuffer next = it.next();
                if (wbb == next) {
                    it.remove();
                }
            }
            System.out.println("Buffer  : " + wbb.getName() + "已经被JVM回收");
        }
    }
}
