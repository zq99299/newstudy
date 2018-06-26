package cn.mrcode.newstudy.hpbase.mysql;

import cn.mrcode.newstudy.hpbase.mysql.mymysql.HandshakeResponse41;
import cn.mrcode.newstudy.hpbase.mysql.mymysql.HandshakeV10;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/22     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/22 13:58
 * @date 2018/6/22 13:58
 * @since 1.0.0
 */
public class TestDemo extends Thread {
    private Selector selector;

    public void init() throws IOException {
        selector = Selector.open();
    }


    @Override
    public void run() {
        try {
            // 选择事件
            while (true) {
                selector.select(500);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    if (selectionKey.isConnectable()) {
                        doConnectable((SocketChannel) selectionKey.channel());
                    } else if (selectionKey.isReadable()) {
                        doRead(selectionKey);
                    } else if (selectionKey.isWritable()) {
                        doWrite();
                    }
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite() throws IOException {
        ByteBuffer buffer;
        while ((buffer = writeQueue.poll()) != null) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        }
    }

    private ByteBuffer readBuffer = ByteBuffer.allocate(16 * 1024 * 1024);
    private long netInBytes = 0;
    protected volatile int readBufferOffset;

    private void doRead(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        int got = channel.read(readBuffer);
        if (got < 0) {
            close(selectionKey, channel);
            return;
        } else if (got == 0
                && !channel.isOpen()) {
            close(selectionKey, channel);
            return;
        }
        netInBytes += got;
        // 循环处理字节信息
        int offset = readBufferOffset, length = 0, position = readBuffer.position();
        for (; ; ) {
            length = getPacketLength(readBuffer, offset);
            if (length == -1) {
                System.out.println("length == -1");
                break;
            }
            if (position >= offset + length) {
                readBuffer.position(offset);
                byte[] data = new byte[length];
                readBuffer.get(data, 0, length);
                handle(data);
                offset += length;
                if (position == offset) {
                    System.out.println("position == offset");
                    break;
                } else {
                    // try next package parse
                    readBufferOffset = offset;
                    readBuffer.position(position);
                    continue;
                }
            } else {
                if (!readBuffer.hasRemaining()) {
                    readBuffer = ensureFreeSpaceOfReadBuffer(readBuffer, offset, length);
                }
                break;
            }
        }
    }

    protected final ConcurrentLinkedQueue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<ByteBuffer>();

    private void handle(byte[] data) throws IOException {
        System.out.println("data:" + data.length);
        switch (data[4]) {
            case 0x00:  // ok 严格的话 包长度 > 7
                System.out.println("处理认证结果");
                break;
            case (byte) 0xff: // err 错误包
                System.out.println("不能连接到mysql");
                break;
            case (byte) 0xfe:  // eof 严格的话 包长度 < 9
                System.out.println("0xfe");
                break;
            default: // 协议字段 9 或 10；握手包/问候包
                System.out.println("都不是，则判定是否认证过了，有可能已经认证过了");
                // 设置握手包
//                HandshakePacket handshakePacket = buildHandShakePacket(data);
//                writeAuthenticate(handshakePacket);

                // 自己实现
                HandshakeV10 handshakeV10 = buildHandShakePacketV10(data);
                writeAuthenticate(handshakeV10);
                break;
        }
    }


    private HandshakeV10 buildHandShakePacketV10(byte[] data) {
        HandshakeV10 packet = new HandshakeV10();
        packet.read(data);
        return packet;
    }

    private void writeAuthenticate(HandshakeV10 handshakeV10) {
        HandshakeResponse41 response41 = new HandshakeResponse41(handshakeV10);
        ByteBuffer response = response41.builderResponse();
        writeQueue.offer(response);
    }

    private HandshakePacket buildHandShakePacket(byte[] data) {
        HandshakePacket packet = new HandshakePacket();
        packet.read(data);
        return packet;
    }

    private void writeAuthenticate(HandshakePacket handshakePacket) throws IOException {
        AuthPacket packet = new AuthPacket(writeQueue);
        packet.packetId = 1;
        packet.clientFlags = 234663;
        packet.maxPacketSize = 16 * 1024 * 1024;
        packet.charsetIndex = handshakePacket.serverCharsetIndex;
        packet.user = "root";
        try {
            packet.password = passwd("123456", handshakePacket);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        packet.database = "mycat_dev_test_1";
        packet.write();
    }

    private static byte[] passwd(String pass, HandshakePacket hs)
            throws NoSuchAlgorithmException {
        if (pass == null || pass.length() == 0) {
            return null;
        }
        byte[] passwd = pass.getBytes();
        int sl1 = hs.seed.length;
        int sl2 = hs.restOfScrambleBuff.length;
        byte[] seed = new byte[sl1 + sl2];
        System.arraycopy(hs.seed, 0, seed, 0, sl1);
        System.arraycopy(hs.restOfScrambleBuff, 0, seed, sl1, sl2);
        return SecurityUtil.scramble411(passwd, seed);
    }

    private ByteBuffer ensureFreeSpaceOfReadBuffer(ByteBuffer buffer,
                                                   int offset, final int pkgLength) {
        // need a large buffer to hold the package
        if (pkgLength > readBuffer.capacity()) {
            throw new IllegalArgumentException("Packet size over the limit. [pkgLength > readBuffer.capacity()]");
        } else if (buffer.capacity() < pkgLength) {
            throw new IllegalArgumentException("Packet size over the limit.[buffer.capacity() < pkgLength]");
        } else {
            if (offset != 0) {
                // compact bytebuffer only
                return compactReadBuffer(buffer, offset);
            } else {
                throw new RuntimeException(" not enough space");
            }
        }
    }

    private ByteBuffer compactReadBuffer(ByteBuffer buffer, int offset) {
        if (buffer == null) {
            return null;
        }
        buffer.limit(buffer.position());
        buffer.position(offset);
        buffer = buffer.compact();
        readBufferOffset = 0;
        return buffer;
    }

    protected int getPacketLength(ByteBuffer buffer, int offset) {
        int headerSize = 4;
//        if ( isSupportCompress() ) {
        // 不压缩
//            headerSize = 7;
//        }

        if (buffer.position() < offset + headerSize) {
            return -1;
        } else {
            int length = buffer.get(offset) & 0xff;
            length |= (buffer.get(++offset) & 0xff) << 8;
            length |= (buffer.get(++offset) & 0xff) << 16;
            return length + headerSize;
        }
    }

    private void close(SelectionKey selectionKey, SocketChannel channel) throws IOException {
        selectionKey.cancel();
        channel.close();
        System.out.println("socket closed");
    }

    private void doConnectable(SocketChannel channel) throws IOException {
        if (channel.isConnectionPending()) {
            channel.finishConnect();
            System.out.println("已链接：" + channel.socket().getLocalPort());
            // 读取数据、然后发送认证包
            System.out.println("读取并发送验证包");
            this.doRead(channel.keyFor(selector));
            // mycat源码中在链接完成之后取消了这个sk；
            // 然后绑定了给另外一个选择器
            System.out.println("增加读写事件");
            channel.keyFor(selector).interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        } else {
            System.out.println("不在链接过程中");
        }
    }

    private SocketChannel channel;

    private void register(String host, int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_CONNECT);
        channel.connect(new InetSocketAddress(host, port));
        this.channel = channel;
    }

    public static void main(String[] args) throws Exception {
        TestDemo testDemo = new TestDemo();
        testDemo.init();
        testDemo.start();
        testDemo.register("localhost", 3306);

        testDemo.join();
    }
}
