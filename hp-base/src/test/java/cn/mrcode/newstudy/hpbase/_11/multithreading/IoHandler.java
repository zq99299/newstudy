package cn.mrcode.newstudy.hpbase._11.multithreading;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/08     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/8 16:18
 * @date 2018/6/8 16:18
 * @since 1.0.0
 */
public class IoHandler implements Runnable {
    private Selector selector;
    private SocketChannel sc;
    private SelectionKey sk;
    private boolean accept;
    private ByteBuffer readerBuffer = ByteBuffer.allocate(100);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024 * 2);
    private Charset GBK = Charset.forName("GBK");

    public IoHandler(Selector selector, SocketChannel accept) {
        this.selector = selector;
        this.sc = accept;
    }

    @Override
    public void run() {
        try {
            if (!accept) {
                sc.configureBlocking(false);
                this.sk = sc.register(selector, 0);
                // 写入欢迎语句
                writeBuffer.put("欢迎进入多线程reactor".getBytes(GBK));
                writeBuffer.flip();
                writeToChannel();
                accept = true;
                sk.interestOps(SelectionKey.OP_READ);
            }
        } catch (Exception e) {

        }
    }
}
