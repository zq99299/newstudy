package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 负责数据的正常交互
 * @author zhuqiang
 * @date 2018/6/28 14:00
 */
public class NIORactor extends Thread {
    private Logger log = LoggerFactory.getLogger(getClass());
    private Selector selector;
    private BlockingQueue<MySqlConnect> registerConnects;

    public NIORactor() throws IOException {
        this.selector = Selector.open();
        registerConnects = new LinkedBlockingQueue();
    }

    @Override
    public void run() {
        // 做选择事件
        try {
            while (true) {
                selector.select(500);
                Set<SelectionKey> keys = selector.selectedKeys();
                registerConnectProcess();
                for (SelectionKey key : keys) {
                    if (key.isConnectable()) {
                        doConnectable(key);
                    } else if (key.isReadable()) {

                    } else if (key.isWritable()) {

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerConnectProcess() throws IOException {
        MySqlConnect connect = null;
        while ((connect = registerConnects.poll()) != null) {
            // 添加到selector中
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            SelectionKey key = channel.register(selector, SelectionKey.OP_CONNECT);
            channel.connect(new InetSocketAddress(connect.getHost(), connect.getPort()));
            key.attach(connect);

            connect.setProcessKey(key);
            connect.setSocketChannel(channel);
        }
    }

    private void doConnectable(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()) {
            // 如果正处于链接过程中
            channel.finishConnect();
            log.info("已链接 " + channel.socket().getLocalPort());
            key.interestOps(0); // 主要目的是去掉链接兴趣
            ((MySqlConnect) key.attachment()).read();
        }
    }

    /**
     * 注册到NIO中
     * @param host
     * @param port
     * @param database 要链接的库
     */
    public void register(String host, int port, String user, String passwd, String database) {
        MySqlConnect connect = new MySqlConnect();
        connect.setHost(host);
        connect.setPort(port);
        connect.setSchema(database);
        connect.setUser(user);
        connect.setPasswd(passwd);
        connect.setHandler(new MySqlAuthHandler(connect));
        registerConnects.offer(connect);
    }
}
