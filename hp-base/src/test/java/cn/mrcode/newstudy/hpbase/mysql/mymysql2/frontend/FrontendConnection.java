package cn.mrcode.newstudy.hpbase.mysql.mymysql2.frontend;

import cn.mrcode.newstudy.hpbase.mysql.mymysql2.MySqlConnect;
import cn.mrcode.newstudy.hpbase.mysql.mymysql2.NIOHandler;
import cn.mrcode.newstudy.hpbase.mysql.mymysql2.SqlConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 前段连接对象
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/3 11:52
 */
public class FrontendConnection implements SqlConnect {
    private Logger log = LoggerFactory.getLogger(getClass());
    private SocketChannel socketChannel;
    private MySqlConnect mySqlConnect;
    private SelectionKey processKey;
    private NIOHandler handler;
    private ConcurrentLinkedQueue<ByteBuffer> writes = new ConcurrentLinkedQueue();

    private int headerSize = 4;
    private int readBufferOffset; // 最后一次读取的位置
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    public FrontendConnection(SocketChannel channel) throws IOException {
        this.socketChannel = channel;
        InetSocketAddress localAddress = (InetSocketAddress) channel.getLocalAddress();
        int localPort = localAddress.getPort();
        String localHost = localAddress.getHostString();
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.getRemoteAddress();
        int remotePort = remoteAddress.getPort();
        String remoteHost = remoteAddress.getHostString();
        log.info("remote port {},host {}", remotePort, remoteHost);
        log.info("local port {},host {}", localPort, localHost);
    }

    /**
     * 前段连接注册：先发送验证包
     */
    public void register() {
        FrontendAuthHandler handler = new FrontendAuthHandler(this);
        this.handler = handler;
        handler.sendHandshakeV10();
    }

    @Override
    public void read() throws IOException {
        int got = socketChannel.read(readBuffer);
        if (got < 0) {
            // 对方已断开了流
            close();
            return;
        }
        int offset = readBufferOffset;
        int position = readBuffer.position();  // 记录当次读取到的有效位置
        while (true) {
            // 包长度 int3
            int length = getPacketLength(readBuffer, offset);
            if (length == -1) {
                log.error("数据包错误,长度 -1");
                break;
            }
            // 保证读取到了一个完整的数据包
            if (position >= offset + length) {
                readBuffer.position(offset);
                byte[] data = new byte[length];
                readBuffer.get(data, 0, length);
                handler.handler(data);

                // 尝试获取下一个包
                offset += length;
                readBufferOffset = offset;  // 记录下一次开始读取的位置
                readBuffer.position(position);
                if (position == offset) {
                    // 如果当前位置已经是有效内容末尾，则跳出循环等待下一次的读
                    log.info("position == offset");
                    break;
                }
                continue;
            } else {
                // position < limit; 由于手动控制的。所以 limit = capacity
                if (!readBuffer.hasRemaining()) {
                    // 确保有更多空闲的空间使用
                    readBuffer = ensureFreeSpaceOfReadBuffer(readBuffer, offset, length);
                }
                break;
            }
        }
    }

    private void close() throws IOException {
        processKey.cancel();
        socketChannel.close();
        log.error("socket closed");
    }

    private int getPacketLength(ByteBuffer readBuffer, int offset) {
        int length = readBuffer.get(offset) & 0xFF;
        length |= (readBuffer.get(++offset) & 0xFF) << 8;
        length |= (readBuffer.get(++offset) & 0xFF) << 16;
        return length + headerSize;
    }

    private ByteBuffer ensureFreeSpaceOfReadBuffer(ByteBuffer readBuffer, int offset, int pkgLength) {
        if (pkgLength > readBuffer.capacity()) {
            throw new IllegalArgumentException("Packet size over the limit. [pkgLength > readBuffer.capacity()]");
        }

        readBuffer.limit(readBuffer.position());
        readBuffer.position(offset);
        readBuffer = readBuffer.compact();
        readBufferOffset = 0;
        return readBuffer;
    }

    @Override
    public void checkWrites() throws IOException {
        ByteBuffer buffer = null;
        while ((buffer = writes.poll()) != null) {
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
        }
        if (writes.isEmpty()) {
            processKey.interestOps(processKey.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }

    public void write(ByteBuffer buffer) {
        writes.offer(buffer);
        processKey.interestOps(processKey.interestOps() | SelectionKey.OP_WRITE);
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setMySqlConnect(MySqlConnect mySqlConnect) {
        this.mySqlConnect = mySqlConnect;
    }

    public MySqlConnect getMySqlConnect() {
        return mySqlConnect;
    }

    public SelectionKey getProcessKey() {
        return processKey;
    }

    public void setProcessKey(SelectionKey processKey) {
        this.processKey = processKey;
    }

    public NIOHandler getHandler() {
        return handler;
    }

    public void setHandler(NIOHandler handler) {
        this.handler = handler;
    }
}
