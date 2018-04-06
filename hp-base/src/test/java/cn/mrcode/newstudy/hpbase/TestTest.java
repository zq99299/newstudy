package cn.mrcode.newstudy.hpbase;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.stream.IntStream;

/**
 * ${todo}
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2017/12/20 22:45
 */
public class TestTest {
    @org.junit.Test
    public void fun1() throws IOException {
        String path = "resources/_02/q04/mbb";
        RandomAccessFile file = new RandomAccessFile(path, "r");
        FileChannel channel = file.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        MappedByteBuffer load = map.load();
        System.out.println(load);
    }

    /**
     * <pre>
     * ByteBuffer 的理解-基本原理：
     *  三个重要参数：
     *      position 位置 ：
     *      capactiy 容量：
     *      limit   上限：
     * </pre>
     */

    @Test
    public void fun2() {
        ByteBuffer buffer = ByteBuffer.allocate(15); // 分配一个15字节的缓冲区
        fun2println(buffer);
        IntStream.range(0, 10).forEach(i -> buffer.put((byte) i)); // 存入十个字节
        fun2println(buffer);
        buffer.flip(); // 重置位置为0，limit 变成存入的十个字符上限，标识有10个位置的数据只有效数据
        fun2println(buffer);
        // 上面因为重置了buffer的位置为0，所以这里读取5个是从头开始读取的
        IntStream.range(0, 5).forEach(i -> System.out.print(buffer.get() + " "));
        System.out.println("");
        fun2println(buffer);
        buffer.rewind(); // 重置位置为0 ，不改变limit
        fun2println(buffer);
        IntStream.range(0, 5).forEach(i -> System.out.print(buffer.get() + " "));
        System.out.println("");
        fun2println(buffer);
        buffer.flip();// 重置，因为上面读取了5个字符，当前位置等于5.重置的时候limit就会变成5.标识只有5个有效数据可读
        fun2println(buffer);
        // 使用hasRemaining api可以验证上面的说法，只会打印5个字符
        // 但是数据一直都在，重置操作只是重置了 标识位
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get() + " ");
        }
    }

    private void fun2println(ByteBuffer buffer) {
        System.out.println(MessageFormat.format("limit={0},capactiy={1},position={2}", buffer.limit(), buffer.capacity(), buffer.position()));
    }

    /**
     * ByteBuffer 的 mark和reset作用示例
     */
    @Test
    public void fun3() {
        ByteBuffer buffer = ByteBuffer.allocate(15); // 分配一个15字节的缓冲区
        IntStream.range(0, 10).forEach(i -> buffer.put((byte) i)); // 存入十个字节
        buffer.flip(); // 重置，切换为读取模式

        fun2println(buffer);
        IntStream.range(0, 5).forEach(i -> {
            System.out.print(buffer.get() + " ");
            if (i == 3) buffer.mark();// 在索引3处标记
        });
        System.out.println("");
        fun2println(buffer);
        buffer.reset();  // 回到上次标记的标志位置,limit 被标志为10
        fun2println(buffer);
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get() + " ");
        }
    }

    /**
     * 内存映射文件示意
     */
    @Test
    public void fun4() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("resources/_02/q04/mbb", "rw");
        FileChannel channel = raf.getChannel();
        MappedByteBuffer mbb = channel.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());// 把文件的所有内容都映射到内存中

        while (mbb.hasRemaining()) {
            System.out.print(mbb.get());
        }

        // 改写buffer中的值，但是要注意，这里的索引不能大于 channel.map中映射的大小
        mbb.putChar(0, '1');
        channel.close();
        raf.close();
        // 这里很奇怪的问题是，在idea中没有看到文件有变化，但是用其他工具打开文件，的确被改写了
    }

    @Test
    public void fun5(){
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.order(ByteOrder.BIG_ENDIAN).getInt();
    }
}