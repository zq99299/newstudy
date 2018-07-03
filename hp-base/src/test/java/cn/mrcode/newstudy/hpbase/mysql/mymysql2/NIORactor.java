package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

import cn.mrcode.newstudy.hpbase.mysql.mymysql2.frontend.FrontendConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * 负责数据的正常交互
 * @author zhuqiang
 * @date 2018/6/28 14:00
 */
public class NIORactor extends Thread {
    private Logger log = LoggerFactory.getLogger(getClass());
    private Selector selector;
    private ConcurrentLinkedQueue<MySqlConnect> registerConnects;

    public NIORactor() throws IOException {
        this.selector = Selector.open();
        registerConnects = new ConcurrentLinkedQueue();
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
//                        ((MySqlConnect) key.attachment()).read();
                        ((SqlConnect) key.attachment()).read();
                    } else if (key.isWritable()) {
//                        ((MySqlConnect) key.attachment()).checkWrites();
                        ((SqlConnect) key.attachment()).checkWrites();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理新加入的连接；完成链接的初始化
     * @throws IOException
     */
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
            key.interestOps(SelectionKey.OP_READ); // 主要目的是覆盖掉连接兴趣
        }
    }

    /**
     * 注册到NIO中
     * @param host
     * @param port
     * @param database 要链接的库
     */
    public void register(String host, int port, String user, String passwd, String database, String charset) {
        MySqlConnect connect = new MySqlConnect();
        connect.setHost(host);
        connect.setPort(port);
        connect.setSchema(database);
        connect.setUser(user);
        connect.setPasswd(passwd);
        connect.setHandler(new MySqlAuthHandler(connect));
        connect.setCharset(Charset.forName(charset));
        if ("utf-8".equalsIgnoreCase(charset)) {
            connect.setCharsetIndex((byte) 33);
        } else if ("gbk".equalsIgnoreCase(charset)) {
            connect.setCharsetIndex((byte) 28);
        } else {
            connect.setCharsetIndex((byte) 8);
        }
        registerConnects.offer(connect);
    }

    /**
     * 当前段连接过来的时候，需要分分配一个后端连接作为与mysql交互的节点
     * 但是之前没有设计连接池，所以这里直接写死一个；只允许接收一个前段连接
     * {@link MySqlAuthHandler#handler(byte[])} 在验证登录成功后，赋值
     */
    public static volatile MySqlConnect mySqlConnect;

    /**
     * 前段交互步骤:
     * 1. 接收前段连接
     * 2. 发送握手包
     * 3. 接收验证包，并响应登录成功
     * 4. 切换验证处理器
     * @param channel
     */
    public void registerFrontend(SocketChannel channel) {
        while (mySqlConnect == null) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            FrontendConnection frontendConnection = new FrontendConnection(channel);
            frontendConnection.setSocketChannel(channel);
            frontendConnection.setMySqlConnect(mySqlConnect);  // 为了简单直观，直接赋值一个后端连接 先走通这个流程
            SelectionKey key = channel.register(selector, SelectionKey.OP_READ, frontendConnection);
            frontendConnection.setProcessKey(key);
            frontendConnection.register();
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
