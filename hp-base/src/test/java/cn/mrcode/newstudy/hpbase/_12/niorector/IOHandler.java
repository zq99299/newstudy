package cn.mrcode.newstudy.hpbase._12.niorector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 业务处理接口
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/12 22:35
 */
public abstract class IOHandler implements Runnable {
    protected final SelectionKey selectionKey;
    protected final ByteBuffer readBuffer;
    protected final SocketChannel socketChannel;

    // 写 不暴露出去，由框架来写
    // 保留未写完的buffer
    private ByteBuffer writeBuffer;
    private LinkedList<ByteBuffer> writeQueue = new LinkedList<>();
    // 当作锁来使用，只能有一个线程写
    private AtomicBoolean writingFlag = new AtomicBoolean(false);

    public IOHandler(Selector selector, SocketChannel sc) throws IOException {
        this.socketChannel = sc;
        sc.configureBlocking(false); // 非阻塞模式
        selectionKey = sc.register(selector, SelectionKey.OP_READ);
        readBuffer = ByteBuffer.allocateDirect(100);
        // 绑定会话
        selectionKey.attach(this);
        this.onConnected();
    }

    /** 链接成功事件 */
    protected abstract void onConnected() throws IOException;

    @Override
    public void run() {
        try {
            if (selectionKey.isReadable()) {
                this.doHandler();
            } else if (selectionKey.isWritable()) {
                doWriteData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 有请求事件 */
    protected abstract void doHandler() throws IOException;

    public void writeData(byte[] data) throws IOException {
        while (!writingFlag.compareAndSet(false, true)) {
            // wait until release  等待被返回
        }
        try {
            ByteBuffer theWriteBuf = writeBuffer;
            if (theWriteBuf == null && writeQueue.isEmpty()) {
                // 都没有积压数据的时候 直接写
                writeToChannel(ByteBuffer.wrap(data));
            } else {
                writeQueue.add(ByteBuffer.wrap(data));
                writeToChannel(theWriteBuf);
            }
        } finally {
            writingFlag.lazySet(false);
        }
    }

    private void doWriteData() throws IOException {
        try {
            while (!writingFlag.compareAndSet(false, true)) {
                // wait until release  等待被返回
            }
            ByteBuffer theWriteBuf = writeBuffer;
            writeToChannel(theWriteBuf);
        } finally {
            writingFlag.lazySet(false);
        }
    }

    private void writeToChannel(ByteBuffer curBuffer) throws IOException {
        if (doWrite(curBuffer)) {
            System.out.println(" block write finished");
            writeBuffer = null;
            if (writeQueue.isEmpty()) {
                System.out.println("... write finished , no more data");
                selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_WRITE
                        | SelectionKey.OP_READ);
            } else {
                ByteBuffer buf = writeQueue.removeFirst();
                buf.flip();
                writeToChannel(buf);
            }
        }
    }

    /**
     * @param curBuffer
     * @return 返回当前buffer是否已经写完
     * @throws IOException
     */
    private boolean doWrite(ByteBuffer curBuffer) throws IOException {
        int writed = socketChannel.write(curBuffer);
        System.out.println("writed " + writed);
        if (curBuffer.hasRemaining()) {
            System.out.println("writed " + writed + " not write finished ,remains " + curBuffer.rewind());
            selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
            if (curBuffer != this.writeBuffer) {
                writeBuffer = curBuffer;
            }
            return false;
        }
        return true;
    }
}
