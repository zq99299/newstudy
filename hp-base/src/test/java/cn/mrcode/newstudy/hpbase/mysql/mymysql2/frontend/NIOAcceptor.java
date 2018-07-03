package cn.mrcode.newstudy.hpbase.mysql.mymysql2.frontend;

import cn.mrcode.newstudy.hpbase.mysql.mymysql2.NIORactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 接收前段连接
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/3 11:17
 */
public class NIOAcceptor extends Thread {
    private int port;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private NIORactor nioRactor;

    public NIOAcceptor(int port, NIORactor nioRactor) throws IOException {
        this.port = port;
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
        this.nioRactor = nioRactor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int select = selector.select(500);
                Set<SelectionKey> keys = selector.selectedKeys();
                for (SelectionKey key : keys) {
                    if (key.isAcceptable()) {
                        accept();
                    }
                }
                keys.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept() {
        try {
            SocketChannel channel = serverSocketChannel.accept();
            channel.configureBlocking(false);
            nioRactor.registerFrontend(channel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
