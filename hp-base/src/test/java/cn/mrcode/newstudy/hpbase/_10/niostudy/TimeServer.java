package cn.mrcode.newstudy.hpbase._10.niostudy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

/**
 * 使用udp通信的示例： 下面的程序 其实就做了一件事情：
 * 接收到一个数据报之后；就回复一个本地时间的毫秒数；其他什么diff_1900 什么的应该是协议里面的；
 * 在客户端的时候还剪掉了该数；所以理论上讲 有没有这个算法 都一样吧？
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/27 14:38
 */
public class TimeServer {
    private static final int DEFAULT_TIME_PORT = 37;
    private static final long DIFF_1900 = 2208988800L;
    protected DatagramChannel channel;

    public TimeServer(int port) throws IOException {
        InetSocketAddress isa = new InetSocketAddress(port);
        this.channel = DatagramChannel.open();
        this.channel.bind(isa);
        System.out.println("服务器已监听端口为 " + port + " 的时间请求");
    }

    public void listen() throws IOException {
        ByteBuffer longBuffer = ByteBuffer.allocate(8);
        longBuffer.order(ByteOrder.BIG_ENDIAN);
        longBuffer.putLong(0, 0);

        longBuffer.position(4);
        ByteBuffer buffer = longBuffer.slice();
        while (true) {
            buffer.clear();
            SocketAddress sa = this.channel.receive(buffer);
            if (sa == null) {
                continue;
            }
            System.out.println("Time request from " + sa);
            buffer.clear();
            longBuffer.putLong(0, (System.currentTimeMillis() / 1000 + DIFF_1900));
            this.channel.send(buffer, sa);
        }
    }

    public static void main(String[] args) throws IOException {
        ByteBuffer longBuffer = ByteBuffer.allocate(8);
        longBuffer.order(ByteOrder.LITTLE_ENDIAN);
        longBuffer.putLong((System.currentTimeMillis() / 1000 + DIFF_1900));
        new TimeServer(9658).listen();
    }
}
