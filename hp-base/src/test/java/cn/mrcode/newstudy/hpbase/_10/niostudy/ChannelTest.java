package cn.mrcode.newstudy.hpbase._10.niostudy;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * 通道测试
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/05/25     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/5/25 16:34
 * @date 2018/5/25 16:34
 * @since 1.0.0
 */
public class ChannelTest {
    @Test
    public void fun1() throws FileNotFoundException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("xx", "r");
        FileChannel channel = randomAccessFile.getChannel();
    }

    public static void main(String[] args) throws IOException {
        new ChannelTest().fun2();
    }

    @Test
    public void fun2() throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(System.in);
        WritableByteChannel wbc = Channels.newChannel(System.out);

//        copyChannel1(rbc, wbc);
        copyChannel2(rbc, wbc);
        rbc.close();
        wbc.close();
    }

    private void copyChannel1(ReadableByteChannel rbc, WritableByteChannel wbc) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        while (rbc.read(buffer) != -1) {
            buffer.flip(); // 切换到写模式
            wbc.write(buffer);
            // 压缩；当buffer中的数据被释放完之后
            // 该操作其实等效于 clear；
            // 压缩的作用：丢弃已释放的数据，把未释放的数据copy到从0开始的位置上
            buffer.compact();
        }
        // 那怎么才能结束呢？奇怪永远都跳不出来的
        buffer.flip();
        while (buffer.hasRemaining()) {
            wbc.write(buffer);
        }
    }

    private void copyChannel2(ReadableByteChannel rbc, WritableByteChannel wbc) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        while (rbc.read(buffer) != -1) {
            buffer.flip(); // 切换到写模式
            while (buffer.hasRemaining()) {
                wbc.write(buffer);
            }
            buffer.clear();
        }
    }

    // Scatter/Gather
    @Test
    public void fun3() {
        ByteBuffer header = ByteBuffer.allocateDirect(2);
        ByteBuffer body = ByteBuffer.allocateDirect(10);
//        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
//        ReadableByteChannel channel = Channels.newChannel(bais);
        ByteBuffer[] buffers = {header, body};
        // 并不是所有的通道都支持该操作，只有几种，文件和socket
//        channel.read(buffers);
    }

    // 文件空洞
    @Test
    public void fun4() throws IOException {
        File tempFile = File.createTempFile("holy", null);
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        FileChannel channel = raf.getChannel();
        // 可以理解为对外内存，创建和开销比较大，但是频繁使用性能提高
        ByteBuffer buffer = ByteBuffer.allocateDirect(100);
        putData(0, buffer, channel);
        putData(50_0000, buffer, channel);
        putData(5000, buffer, channel);
        channel.close();
        raf.close();
    }

    private void putData(int position, ByteBuffer buffer, FileChannel channel) throws IOException {
        String str = "*<-- location " + position;
        buffer.clear();
//        buffer.put(str.getBytes("US-ASCII"));
        buffer.put(str.getBytes());
        buffer.flip();
        channel.position(position);
        channel.write(buffer);
    }

    // 文件锁定;锁定文件区块以后有时间再测试
    @Test
    public void fun5() throws IOException {
        String file = "C:\\Users\\mrcode\\Desktop\\新建文本文档.txt";
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel fc = raf.getChannel();
        FileLock lock = fc.lock();
        lock.release();
        // 下面这种方式获得的就是一个只读通道
        SeekableByteChannel channel = Files.newByteChannel(Paths.get(file));
        FileChannel fileChannel = (FileChannel) channel;

        // 调用写或则lock独占锁都会被抛出异常
        fileChannel.write(ByteBuffer.wrap("xxx".getBytes()));
        fileChannel.lock();
        System.out.println(channel);
    }

    // 文件共享锁的测试实验
    // 结果：windows下不能共享锁；会报重叠错误
    @Test
    public void fun6() throws IOException {
        String file = "C:\\Users\\mrcode\\Desktop\\新建文本文档.txt";
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel fc = raf.getChannel();
        FileLock lock = fc.lock(0, 10, true);

        RandomAccessFile raf2 = new RandomAccessFile(file, "r");
        FileChannel fc2 = raf2.getChannel();
        FileLock lock2 = fc2.lock(0, 10, true);

    }

    // 阻塞模式的切换与锁定
    @Test
    public void fun7() throws IOException, InterruptedException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        Object blockingLock = ssc.blockingLock();
        Thread t1 = new Thread(() -> {
            // 现在拥有该锁，通道模式将不能被其他线程改变
            synchronized (blockingLock) {
                try {
                    boolean prevState = ssc.isBlocking();
                    System.out.println(Thread.currentThread().getName() + " : " + prevState);
                    ssc.configureBlocking(false);
                    TimeUnit.SECONDS.sleep(5);
                    // 模拟在此之间进行一些操作
                    // ssc.accept()
                    // 在吧模式切换回去；但是有什么用处呢？
                    ssc.configureBlocking(prevState);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                // isBlocking 是同步操作，上面返回的加锁对象
//                boolean prevState = ssc.isBlocking();
//                System.out.println(Thread.currentThread().getName() + " : " + prevState + " 开始修改模式");
                ssc.configureBlocking(false);
                System.out.println(Thread.currentThread().getName() + " : 修改模式成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    // 通道是否有一个对等的socket测试
    // 结论有：但是ServerSocketChannel.bind后.socket()一开始是没有socket对象的
    // 所以这个对象是在哪里创建的呢? 很疑惑
    @Test
    public void fun8() throws IOException {
        ServerSocketChannel open = ServerSocketChannel.open();
        InetSocketAddress isa = new InetSocketAddress(9656);
//        open.bind(isa);
        open.socket().bind(isa);
        SocketChannel sc = open.accept(); // 如果没有绑定一个端口将会异常 NotYetBoundException
        System.out.println("有人已链接");
    }

    @Test
    public void fun9() throws IOException {
        InetSocketAddress isa = new InetSocketAddress(9656);
        SocketChannel sc = SocketChannel.open(isa);
        sc.bind(isa);
        System.out.println("已链接");
    }

    // 不使用选择器的时候 使用非阻塞模式
    @Test
    public void fun10() throws IOException, InterruptedException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress isa = new InetSocketAddress(9656);
        ssc.bind(isa);
        ssc.configureBlocking(false);
        while (true) {
            SocketChannel accept = ssc.accept();
            if (accept == null) {
                TimeUnit.MILLISECONDS.sleep(500);
                continue;
            }
            System.out.println("已链接");
        }
    }

    // fun11 和 12 测试猜测：不要ServerSocket是否可以直接链接
    // 结论：不可以
    @Test
    public void fun11() throws IOException {
        InetSocketAddress isa = new InetSocketAddress(9656);
        SocketChannel sc = SocketChannel.open();
        sc.bind(isa);
        ByteBuffer buffer = ByteBuffer.allocate(10);
        while (buffer.position() == 0) {
            sc.read(buffer);  // 读取方法必须要链接到一个ServerSocket
        }
        buffer.flip();
        System.out.println(buffer);
    }

    @Test
    public void fun12() throws IOException {
        InetSocketAddress isa = new InetSocketAddress(9656);
        SocketChannel sc = SocketChannel.open();
        sc.connect(isa);
        System.out.println(sc.isConnectionPending());
    }

    @Test
    public void fun13() throws IOException {

    }
}
