package cn.mrcode.newstudy.hpbase._12.myniorector;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 具体的事件处理架子
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/14 23:30
 */
public abstract class MyIOHandler implements Runnable {
    protected final SelectionKey sk;
    protected final SocketChannel sc;
    // 写 不暴露出去，由框架来写
    // 保留未写完的buffer
    private ByteBuffer writeBuffer;
    private LinkedList<ByteBuffer> writeQueue = new LinkedList<>();
    // 当作锁来使用，只能有一个线程写
    private AtomicBoolean writingFlag = new AtomicBoolean(false);

    public MyIOHandler(SelectionKey sk) throws IOException {
        this.sk = sk;
        sc = (SocketChannel) sk.channel();
        this.onConnected();
        sk.attach(this);
    }

    @Override
    public void run() {
        try {
            if (sk.isReadable()) {

                doHandler();

            } else if (sk.isWritable()) {
                doWrite();
            }
        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.closeQuietly(sc);
        }
    }

    /** 链接成功事件 */
    protected abstract void onConnected() throws IOException;

    protected abstract void doHandler() throws IOException;

    private void doWrite() throws IOException {
        System.out.println("写数据...");
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

    public void write(byte[] data) throws IOException {
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

    private void writeToChannel(ByteBuffer curBuffer) throws IOException {
        if (doWrite(curBuffer)) {
            System.out.println(" block write finished");
            writeBuffer = null;
            if (writeQueue.isEmpty()) {
                System.out.println("... write finished , no more data");
                sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE
                        | SelectionKey.OP_READ);
                sk.selector().wakeup();
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
        int writed = sc.write(curBuffer);
        System.out.println("writed " + writed);
        if (curBuffer.hasRemaining()) {
            System.out.println("writed " + writed + " not write finished ,remains " + curBuffer.rewind());
            if (curBuffer != this.writeBuffer) {
                writeBuffer = curBuffer;
            }
            sk.interestOps(sk.interestOps() | SelectionKey.OP_WRITE);
            sk.selector().wakeup();
            return false;
        }
        return true;
    }
}
