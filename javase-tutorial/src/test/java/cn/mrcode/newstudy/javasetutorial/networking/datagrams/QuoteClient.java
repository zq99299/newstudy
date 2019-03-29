package cn.mrcode.newstudy.javasetutorial.networking.datagrams;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * <pre>
 * ${desc}
 * </pre>
 *
 * @author zhuqiang
 * @date 2019/3/29 16:06
 */
public class QuoteClient {
    public static void main(String[] args) throws IOException {
        args = new String[]{"localhost"};

        // 通控制台传递进来要发送数据的服务器
        if (args.length != 1) {
            System.out.println("Usage: java QuoteClient <hostname>");
            return;
        }

        // 构建一个 DatagramSocket
        // get a datagram socket
        DatagramSocket socket = new DatagramSocket();

        // send request
        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName(args[0]);
        // 通过 ip 和 端口发送空的数据包
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);

        // get response
        packet = new DatagramPacket(buf, buf.length);
        // 发完之后就阻塞接收服务器的响应
        socket.receive(packet);

        // 从二进制数据变成字符串打印
        // display response
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Quote of the Moment: " + received);

        socket.close();
    }
}
