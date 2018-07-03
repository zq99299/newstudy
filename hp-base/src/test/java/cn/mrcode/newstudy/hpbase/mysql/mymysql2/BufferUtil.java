package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

import java.nio.ByteBuffer;

/**
 * 与 {@link MySQLMessage} 中的读是相反的
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/3 13:08
 */
public class BufferUtil {
    public static void writeBytesWithNull(ByteBuffer buffer, byte[] data) {
        buffer.put(data);
        buffer.put((byte) 0);
    }

    public static void writeStringWithNull(ByteBuffer buffer, String serverVersion) {
        writeBytesWithNull(buffer, serverVersion.getBytes());
    }

    public static final void writeUB2(ByteBuffer buffer, int i) {
        buffer.put((byte) (i & 0xff));
        buffer.put((byte) (i >>> 8));
    }

    public static void writeUB4(ByteBuffer buffer, long val) {
        buffer.put((byte) (val & 0xff));
        buffer.put((byte) (val >>> 8));
        buffer.put((byte) (val >>> 16));
        buffer.put((byte) (val >>> 24));
    }
}
