package cn.mrcode.newstudy.hpbase._11.multithreading;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/9 16:15
 */
public class DownloadFileClient {
    private ByteBuffer buffer = ByteBuffer.allocate(100);

    private boolean download = false;
    private FileChannel fileChannel = null;
    // 这里简单快速的测试 接收文件的速度；所以直接写死了接收文件的大小
    // 如果要完美的话。需要服务端 对报文进行设计
    private long fileSize = 104857600;
    private long lastFilePost = 0;
    private long receiveBufferSize;
    public static String rootDir = IoHandler.class.getClassLoader().getResource("_11").getFile().substring(1);
    private Path testPath = null;
    private Instant start;
    private volatile boolean runing = true;

    public void start(int port) {
        try {
            Selector selector = Selector.open();
            InetSocketAddress isa = new InetSocketAddress(port);
            SocketChannel sc = SocketChannel.open(isa);
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
            while (runing) {
                int select = selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                if (it.hasNext()) {
                    SelectionKey sk = it.next();
                    if (sk.isReadable()) {
                        // 由于是单线程；所以不用处理兴趣事件
                        doRead(selector, sk, sc);
                    }
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doRead(Selector selector, SelectionKey sk, SocketChannel sc) throws IOException {
        if (!download) {
            int read = sc.read(buffer);
            buffer.flip();
            System.out.println(Charset.forName("GBK").decode(buffer));
            buffer.clear();
            buffer.put("download\r\n".getBytes());
            buffer.flip();
            sc.write(buffer);
            download = true;
            start = Instant.now();
            testPath = Paths.get(rootDir, "test_download" + ThreadLocalRandom.current().nextInt(100) + ".txt");
        }
        if (fileChannel == null) {
            fileChannel = FileChannel.open(testPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            receiveBufferSize = sc.socket().getReceiveBufferSize();
        }
        lastFilePost += fileChannel.transferFrom(sc, lastFilePost, receiveBufferSize * 2);
        // System.out.println(String.format(String.format("%s 文件传输进度：%d / %d", Thread.currentThread().getName(), lastFilePost, fileSize)));
        if (lastFilePost == fileSize) {
            System.out.println(String.format("%s 文件下载成功 %s", Thread.currentThread().getName(), testPath));
            System.out.println(String.format("%s 耗时 %d", Thread.currentThread().getName(), Duration.between(start, Instant.now()).toMillis()));
            runing = false;
            selector.close();
        }
    }
}
