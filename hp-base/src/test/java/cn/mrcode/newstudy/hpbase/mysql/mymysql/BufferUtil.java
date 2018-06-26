package cn.mrcode.newstudy.hpbase.mysql.mymysql;

import java.nio.ByteBuffer;

/**
 * mysql 协议写入工具类
 * <pre>
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/26 13:09
 * @date 2018/6/26 13:09
 * @since 1.0.0
 */
public class BufferUtil {
    public static void writeUB4(ByteBuffer buffer, long l) {
        buffer.put((byte) l);
        buffer.put((byte) (l >>> 8));
        buffer.put((byte) (l >>> 16));
        buffer.put((byte) (l >>> 24));
    }

    public static void writeBytesWithNull(ByteBuffer buffer, byte[] datas) {
        buffer.put(datas);
        buffer.put((byte) 0);
    }

    public static void writeStringWithNull(ByteBuffer buffer, String body) {
        writeBytesWithNull(buffer, body.getBytes());
    }

    public static void writeStringFix(ByteBuffer buffer, byte[] password) {
        buffer.put(password);
    }
}
