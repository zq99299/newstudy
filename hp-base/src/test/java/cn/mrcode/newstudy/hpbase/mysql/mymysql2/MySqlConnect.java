package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

import cn.mrcode.newstudy.hpbase.mysql.BufferUtil;
import cn.mrcode.newstudy.hpbase.mysql.Capabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 链接对象，存储链接相关信息
 * @author zhuqiang
 * @date 2018/6/28 14:03
 */
public class MySqlConnect {
    private Logger log = LoggerFactory.getLogger(getClass());
    public static int DEFAULT_PAKET_SIZE = 16 * 1024 * 1024;
    private int headerSize = 4;
    private String host;
    private int port;
    private String schema;
    private NIOHandler handler;
    private SelectionKey processKey;
    private SocketChannel socketChannel;
    private String user;
    private String passwd;

    private int readBufferOffset; // 最后一次读取的位置
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    private ConcurrentLinkedQueue<ByteBuffer> writes = new ConcurrentLinkedQueue();
    private Charset charset;
    private byte charsetIndex;

    public static int getClientCapabilities() {
        int flag = 0;
        flag |= Capabilities.CLIENT_LONG_PASSWORD;
        flag |= Capabilities.CLIENT_FOUND_ROWS;
        flag |= Capabilities.CLIENT_LONG_FLAG;
        flag |= Capabilities.CLIENT_CONNECT_WITH_DB;
        flag |= Capabilities.CLIENT_ODBC;
        flag |= Capabilities.CLIENT_IGNORE_SPACE;
        flag |= Capabilities.CLIENT_PROTOCOL_41;
        flag |= Capabilities.CLIENT_INTERACTIVE;
        flag |= Capabilities.CLIENT_IGNORE_SIGPIPE;
        flag |= Capabilities.CLIENT_TRANSACTIONS;
        flag |= Capabilities.CLIENT_SECURE_CONNECTION;
        flag |= Capabilities.CLIENT_PLUGIN_AUTH;
        return flag;
    }

    /**
     * 读数据要做的工作：
     * 1. 接收来自mysql服务器发来的所有数据包，并解析
     * 2. 做好相关解析操作
     * @throws IOException
     */
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

    /**
     * 写 要做的事情有：
     * 1. 接收处理器或则其他地方来发来的数据包。 要发送到mysql服务器的
     * 2. 接收ractor的写事件，实施实际的写操作
     * 3. 如果已经写完则要暂时 退订ractor的写事件，防止一直空转
     * <p>
     * 所以与nio相关的操作 都可以封装到一个对象中去实施；
     * @param buffer
     */
    public void write(ByteBuffer buffer) {
        writes.offer(buffer);
        processKey.interestOps(processKey.interestOps() | SelectionKey.OP_WRITE);
    }

    /**
     * 检查并发送
     */
    public void checkWrites() throws IOException {
        // 这里先直接遍历写。阻塞的写。后面再修改成nio的写
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

    /**
     * 执行sql语句
     * @param sql
     */
    public void execSQL(String sql) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.position(3);
        buffer.put((byte) 0);
        buffer.put((byte) 3);
        // string.eof
        // 如果字符串是数据包的最后一个组件，那么它的长度可以从总体包长度减去当前位置来计算。
        // 也就是说 只要是这个类型的。直接获取到包头然后 - 1个字节的命令类型 就得到长度了
        // .eof与其他类型的不同。不需要修饰
        buffer.put(sql.getBytes());
        int position = buffer.position();
        buffer.position(0);
        BufferUtil.writeInt(buffer, position - 4);
        buffer.position(position);
        buffer.flip();
        write(buffer);
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

    private int getPacketLength(ByteBuffer readBuffer, int offset) {
        int length = readBuffer.get(offset) & 0xFF;
        length |= (readBuffer.get(++offset) & 0xFF) << 8;
        length |= (readBuffer.get(++offset) & 0xFF) << 16;
        return length + headerSize;
    }

    private void close() throws IOException {
        processKey.cancel();
        socketChannel.close();
        log.info("socket closed");
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        return schema;
    }

    public void setHandler(NIOHandler handler) {
        this.handler = handler;
    }

    public NIOHandler getHandler() {
        return handler;
    }

    public void setProcessKey(SelectionKey processKey) {
        this.processKey = processKey;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharsetIndex(byte charsetIndex) {
        this.charsetIndex = charsetIndex;
    }

    public byte getCharsetIndex() {
        return charsetIndex;
    }
}
