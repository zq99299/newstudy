package cn.mrcode.newstudy.hpbase._12.myniorector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 具体的事件处理架子
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/14 23:30
 */
public abstract class MyIOHandler implements Runnable {
    private final SelectionKey sk;
    private final SocketChannel sc;

    public MyIOHandler(SelectionKey sk) throws IOException {
        this.sk = sk;
        sc = (SocketChannel) sk.channel();
        this.onConnected();
    }

    @Override
    public void run() {
        if (sk.isReadable()) {
            sc.read()
        } else if (sk.isWritable()) {

        }
    }

    /** 链接成功事件 */
    protected abstract void onConnected() throws IOException;

    public void write(byte[] data) throws IOException {
        sc.write(ByteBuffer.wrap(data));
    }
}
