package cn.mrcode.newstudy.hpbase._10.niostudy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * udp 协议测试
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/27 13:24
 */
public class TimeClient {
    private static final int DEFAULT_TIME_PORT = 37;
    private static final long DIFF_1900 = 2208988800L;
    protected int port = DEFAULT_TIME_PORT;
    protected List<InetSocketAddress> remoteHosts;
    protected DatagramChannel channel;

    public TimeClient(int port) throws IOException {
        this.port = port;
        this.channel = DatagramChannel.open();
    }

    protected void sendRequests() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        Iterator<InetSocketAddress> it = remoteHosts.iterator();
        while (it.hasNext()) {
            InetSocketAddress isa = it.next();
            System.out.println("send : " + isa.getHostName() + ":" + isa.getPort());
            // 时间协议 RFC868
            buffer.clear().flip();
            channel.send(buffer, isa);
        }
    }

    // 接收回复;
    public void getReplies() throws IOException {
        ByteBuffer longBuffer = ByteBuffer.allocate(8);
        longBuffer.order(ByteOrder.BIG_ENDIAN);
        longBuffer.putLong(0, 0);
        longBuffer.position(4); // 4字节
        ByteBuffer buffer = longBuffer.slice();
        int expect = remoteHosts.size();
        int replies = 0;
        System.out.println("");
        System.out.println("等待回复");
        while (true) {
            // 一个long 占8个字节；这里使用4字节的进行读取 怎么也没有问题呢？
            // 在该访问的数值内，只有4个字节有数据;
            // 对于大头模式 数据到底是怎么的 ，现在也没有搞清楚；但是 提供的buffer不够数据包的大小。将丢弃装不下的部分
            InetSocketAddress isa = receivePacket(channel, buffer);
            buffer.flip();
            replies++;

            printTime(longBuffer.getLong(0), isa);
            if (replies == expect) {
                System.out.println("已接收所有的回复");
                break;
            }
            // 有些回复还没有出现
            System.out.println("回复进度" + replies + " / " + expect);
        }
    }

    private void printTime(long remote1900, InetSocketAddress isa) {
        long local = System.currentTimeMillis() / 1000;
        long remote = remote1900 - DIFF_1900;
        Date remoteDate = new Date(remote * 1000);
        Date localDate = new Date(local * 1000);
        long skew = remote - local;
        System.out.println("reply from " + isa.getHostName() + ":" + isa.getPort());
        System.out.println("remote:" + remoteDate);
        System.out.println("local:" + localDate);
        if (skew == 0) {
            System.out.println("none");
        } else if (skew > 0) {
            System.out.println("远程比本地快 " + skew + " 秒");
        } else {
            System.out.println("本地比远程快 " + (-skew) + " 秒");
        }
    }

    protected InetSocketAddress receivePacket(DatagramChannel channel, ByteBuffer buffer) throws IOException {
        buffer.clear();
        return (InetSocketAddress) channel.receive(buffer);
    }

    public static void main(String[] args) throws IOException {

        TimeClient timeClient = new TimeClient(9658);
        timeClient.remoteHosts = new ArrayList<>();
        timeClient.remoteHosts.add(new InetSocketAddress("localhost", 9658));
        timeClient.remoteHosts.add(new InetSocketAddress("localhost", 9658));
        timeClient.remoteHosts.add(new InetSocketAddress("localhost", 9658));
        timeClient.sendRequests();
        timeClient.getReplies();
    }
}
