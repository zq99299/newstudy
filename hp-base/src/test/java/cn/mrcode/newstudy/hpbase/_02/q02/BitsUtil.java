package cn.mrcode.newstudy.hpbase._02.q02;

/**
 * 大头模式： 二进制中的高位存储在数组(内存地址)底地址（索引0）
 * 小头模式：二进制中的高位存储在高地址
 * java.nio.ByteOrder 一个值大小头模式怎么分辨。虽然没看懂；先记录
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/6 22:00
 */
public class BitsUtil {

    public static byte[] convertBigItem(int a) {
        byte[] bytes = new byte[4];
        // 利用强转把高位去掉的特性
        bytes[3] = (byte) a;
        bytes[2] = (byte) (a >>> 8);
        bytes[1] = (byte) (a >>> 16);
        bytes[0] = (byte) (a >>> 24);
        return bytes;
    }

    public static int getBigItem(byte[] bytes) {
        // 这里为什么要 & 0xFF? 目的是把一个byte转换成32位的二进制,高位全部为0
        // 所以如果这个8位原先不是在 低位的，只要移动到指定位置就还原了；
        /**
         * 1000 0010 -2的源码
         * 1111 1101 -2的反码
         * 111111111111111111111111 1111 1110 -2的补码
         * 000000000000000000000000 1111 1111 0xFF
         * ----------------------------------
         * 000000000000000000000000 1111 1110 254（十进制）
         */
        // 要记住一点，而且是非常重要：位运算是针对整型的，进行位操作时，除long外，其他的类型会自动转成int
        // 所以这里为什么 & 运算之后，一个负数就变成了正数。
        // 这里我按照8位来算，始终都没有搞明白为什么8位的开头是1，却是一个正数
        // java 中的类型是有符号的
        return (bytes[3] & 0xFF)
                + ((bytes[2] & 0xFF) << 8)
                + ((bytes[1] & 0xFF) << 16)
                + ((bytes[0] & 0xFF) << 24);
    }

    public static byte[] convertLittleItem(int a) {
        byte[] bytes = new byte[4];
        // 利用强转把高位去掉的特性
        bytes[0] = (byte) a;
        bytes[1] = (byte) (a >>> 8);
        bytes[2] = (byte) (a >>> 16);
        bytes[3] = (byte) (a >>> 24);
        return bytes;
    }

    public static int getLittleItem(byte[] bytes) {
        // 利用强转把高位去掉的特性
        return (bytes[0] & 0xFF)
                + ((bytes[1] & 0xFF) << 8)
                + ((bytes[2] & 0xFF) << 16)
                + ((bytes[3] & 0xFF) << 24);
    }

    public static void main(String[] args) {
        System.out.println(getBigItem(convertBigItem(1000)));
        System.out.println(getLittleItem(convertLittleItem(1000)));
    }
}
