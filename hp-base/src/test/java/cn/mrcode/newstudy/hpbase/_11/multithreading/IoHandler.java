package cn.mrcode.newstudy.hpbase._11.multithreading;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/08     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/8 16:18
 * @date 2018/6/8 16:18
 * @since 1.0.0
 */
public class IoHandler implements Runnable {
    private Selector selector;
    private SocketChannel sc;
    private SelectionKey sk;
    private boolean accept;
    private ByteBuffer readerBuffer = ByteBuffer.allocate(100);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024 * 2);
    private Charset GBK = Charset.forName("GBK");
    private int lastPos;

    public IoHandler(Selector selector, SocketChannel accept) throws IOException {
        this.selector = selector;
        this.sc = accept;
        sc.configureBlocking(false);
        // 注册不能多线程注册，否则异常
        this.sk = sc.register(selector, 0);
        sk.attach(this);
    }


    @Override
    public void run() {
        try {
            if (!accept) {
                // 写入欢迎语句
                writeBuffer.put("欢迎进入多线程reactor\r\nTelnet>".getBytes(GBK));
                writeBuffer.flip();
                writeToChannel();
                accept = true;
                return;
            }
            if (sk.isReadable()) {
                doRead();
            } else if (sk.isWritable()) {
                // 正在传输文件
                if (fileChannel != null) {
                    transferFile();
                    return;
                }
                writeToChannel();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 运行出错就关闭
            sk.cancel();
            IOUtils.closeQuietly(sk.channel());
        }
    }

    private void doRead() throws IOException {
        System.out.println("读事件处理");
        int read = sc.read(readerBuffer);
        System.out.println("当次读取到 " + read);
        int readEnd = readerBuffer.position(); // 当前读取到有效数据位置
        // 寻找一行
        String lineCommond = null;
        for (int i = lastPos; i < readEnd; i++) {
            if (readerBuffer.get(i) == 13) {
                int lineSize = i - lastPos;
                byte[] lineBytes = new byte[lineSize];
                readerBuffer.position(lastPos); // 从上一次位置开始读
                readerBuffer.get(lineBytes);
                lineCommond = new String(lineBytes);
                lastPos = i;
            }
        }
        // 处理命令
        if (lineCommond != null) {
            System.out.println("lineCommond " + lineCommond);
            processCommond(lineCommond);
        } else {
            sk.interestOps(sk.interestOps() | SelectionKey.OP_READ);
        }

        // 清理读缓存
        // 已使用一半的空间，则压缩空间，丢弃掉已读数据
        // 但是这里有一个bug；当一行命令超过51的时候每次都会调用该方法
        if (readerBuffer.position() > readerBuffer.capacity() / 2) {
            readerBuffer.limit(readerBuffer.position());
            readerBuffer.position(lastPos);
            readerBuffer.compact();
            lastPos = 0;
        }
        selector.wakeup();
    }

    private void processCommond(String lineCommond) throws IOException {
        // 只按回车，也会造成字符串对象为空，但是为了模拟客户端的前缀Telnet> ；每次按回车都打印该字符串
        if (lineCommond.isEmpty()) {
            writeBuffer.put("\r\nTelnet>".getBytes());
            writeBuffer.flip();
            writeToChannel();
            return;
        }
        if (lineCommond.startsWith("download")) {
            // 固定下载一个文件
            // 使用 FileChannel的transferTo方法
            download();
        } else {
            // 模拟写入一些字符串
            for (int i = 0; i < writeBuffer.capacity() - 9; i++) {
                // a = 97 ; z = 122 下面这个25 = 122 -97
                writeBuffer.put((byte) (i % 25 + 'a'));
            }
            writeBuffer.put("\r\nTelnet>".getBytes());
            writeBuffer.flip();
            writeToChannel();
        }
    }

    private void writeToChannel() throws IOException {
        int writed = sc.write(writeBuffer);
        System.out.println("已写 " + writed);
        if (writeBuffer.hasRemaining()) {
            System.out.println("还未写完数据,剩余 " + writeBuffer.remaining());
        } else {
            System.out.println("已写完");
            writeBuffer.clear();
            sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
            // 可以继续处理命令
            sk.interestOps(sk.interestOps() | SelectionKey.OP_READ);
            // 在多线程情况下，如果不wakeup；那么将会存在一直阻塞
            // 原因是什么呢？ 是因为就绪选择过程 要提前有兴趣？
            selector.wakeup();
        }
    }


    public static String rootDir = IoHandler.class.getClassLoader().getResource("_11").getFile().substring(1);
    private FileChannel fileChannel = null;
    private long fileSize = 0;
    private long lastFilePost = 0;
    private long sendBufferSize;

    private void download() throws IOException {
        System.out.println("文件开始下载");
        // 生成100m的文件
        Path testPath = Paths.get(rootDir, "test.txt");
        // 在Windows下默认写入ASSIC编码；一个字母占用1byte
/*        int fileSize = 100 * 1024 * 1024 - 9; // 100M
        try (BufferedWriter bw = Files.newBufferedWriter(testPath)) {
            for (int i = 0; i < fileSize; i++) {
                bw.write((char) ('a' + (i % 25)));
            }
            bw.write("\r\nTelnet>");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        fileChannel = FileChannel.open(testPath);
        fileSize = fileChannel.size();
        sendBufferSize = sc.socket().getSendBufferSize();
        transferFile();
    }

    private void transferFile() throws IOException {
        lastFilePost += fileChannel.transferTo(lastFilePost, sendBufferSize * 2, sc);
        if (lastFilePost == fileSize) {
            // 文件已经传输完成
            // 恢复读事件
            System.out.println("文件已经下载完成");
            fileChannel.close();
            fileChannel = null;
            lastFilePost = 0;
            fileSize = 0;
            sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
            sk.interestOps(sk.interestOps() | SelectionKey.OP_READ);
        } else {
            sk.interestOps(sk.interestOps() | SelectionKey.OP_WRITE);
            System.out.println(String.format("文件传输进度：%d / %d", lastFilePost, fileSize));
        }
        selector.wakeup();
    }
}
