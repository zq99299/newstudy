package cn.mrcode.newstudy.hpbase._02.q02;

/**
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
        // 利用强转把高位去掉的特性
        return (bytes[0] << 24)
                + (bytes[1] << 16)
                + (bytes[2] << 8)
                + (bytes[3]);
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
        return (bytes[3] << 24)
                + (bytes[2] << 16)
                + (bytes[1] << 8)
                + (bytes[0]);
    }
}
