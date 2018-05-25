package cn.mrcode.newstudy.hpbase._10.niostudy;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.stream.IntStream;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/05/25     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/5/25 16:33
 * @date 2018/5/25 16:33
 * @since 1.0.0
 */
public class TestDemo {

    // byteBuffer 数据获取相关测试
    @Test
    public void get() {
        ByteBuffer bf = ByteBuffer.allocate(20);
        IntStream.range(0, 20).forEach(i -> bf.put((byte) i));
        bf.flip();
        bf.get(); // 单个获取 会修改 pos
        bf.get(new byte[10]); // 批量获取等同于 get(dst, 0, dst.length);
        byte[] dst = new byte[10];
        int length = Math.min(bf.remaining(), dst.length);
        // 下面这个用法是不对的，平时都以为是传递数组的索引
        // 而这里不是start是 offset
        // 什么意思呢？偏移量是是获取的元素从目标数组中的哪个索引位置开始存储，并存储多少个
        // 这里要想读取剩下的9个元素，offset 必须写0；
        // 或则写1；因为只有9个元素，传入的数据可以容纳10个元素
        bf.get(dst, 1, length);
        System.out.println(dst);
    }

    // byteBuffer 数据存储相关测试
    @Test
    public void put() {
        ByteBuffer bf = ByteBuffer.allocate(20);
        ByteOrder order = bf.order();
        bf.put((byte) 1);
        bf.put(new byte[]{2, 3, 4, 5, 6, 7, 8, 9, 10});
        byte[] dst = {11, 12, 13, 14, 15, 16, 17, 18, 19};
        int length = Math.min(bf.remaining(), dst.length);
        // put 也是同理，后面两个参数都是针对 dst 数组；
        bf.put(dst, 1, 8);
        // 当然如果要存入的数据buffer不够装，则抛出异常了
        // java.nio.BufferOverflowException
        bf.put(dst, 1, 8);
    }

    // 使用提供的存储基础类型来存储数据
    @Test
    public void fun3() {
        ByteBuffer allocate = ByteBuffer.allocate(10);
        allocate.putInt(0, (258 & 0xffffffff));
        System.out.println(allocate.getInt() & 0xffffffff);
    }
}
