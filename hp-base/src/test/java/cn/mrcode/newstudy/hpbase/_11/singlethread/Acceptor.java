package cn.mrcode.newstudy.hpbase._11.singlethread;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 接收链接
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/6 22:22
 */
public class Acceptor extends Thread {
    private SelectionKey sk;

    public Acceptor(SelectionKey sk) throws IOException {
        this.sk = sk;
    }

    @Override
    public void run() {
        SocketChannel sc = null;
        try {
            sc = ((ServerSocketChannel) sk.channel())
                    .accept();
            sc.configureBlocking(false);
            new IoHandler(sk.selector(), sc);
            System.out.println("新的链接：" + Thread.currentThread().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
