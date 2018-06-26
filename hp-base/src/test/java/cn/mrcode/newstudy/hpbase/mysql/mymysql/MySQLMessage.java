package cn.mrcode.newstudy.hpbase.mysql.mymysql;

/**
 * 各种字节读取;
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/24 22:18
 */
public class MySQLMessage {
    private final byte[] data;
    private final int length;
    private int position;

    public MySQLMessage(byte[] data) {
        this.data = data;
        this.length = data.length;
        this.position = 0;
    }

    /**
     * 对于int<n>来说是无符号整数，使用需要转换
     * @return
     */
    public int readUB2() {
        final byte[] b = this.data;
        int i = b[position++] & 0xff;
        i |= (b[position++] & 0xff) << 8;
        return i;
    }

    /** 三字节凑成的一个整数 */
    public int readUB3() {
        final byte[] b = this.data;
        // & 0xff 是转成无符号
        int i = b[position++] & 0xff;
        // | 相当于加
        i |= (b[position++] & 0xff) << 8;
        i |= (b[position++] & 0xff) << 16;
        return i;
    }

    public long readUB4() {
        final byte[] b = this.data;
        long i = (long) b[position++] & 0xff;
        i |= (long) (b[position++] & 0xff) << 8;
        i |= (long) (b[position++] & 0xff) << 16;
        i |= (long) (b[position++] & 0xff) << 24;
        return i;
    }

    public byte read() {
        return data[position++];
    }

    /**
     * 以 0x00 结尾
     * @return
     */
    public byte[] readBytesWithNull() {
        final byte[] b = this.data;
        int offset = -1;
        for (int i = position; i < length; i++) {
            if (b[i] == 0) {
                offset = i;
                break;
            }
        }
        // 先不考虑健壮性
        byte[] bytes = new byte[offset - position];
        System.arraycopy(b, position, bytes, 0, bytes.length);
        position = (offset + 1);  // position 指向下一个可读数据
        return bytes;
    }

    public String readStringWithNull() {
        return new String(readBytesWithNull());
    }

    /**
     * 读取固定字符串
     * @param num
     * @return
     */
    public byte[] readByteFix(int num) {
        byte[] bytes = new byte[num];
        System.arraycopy(data, position, bytes, 0, bytes.length);
        position += num;
        return bytes;
    }

    public String readStringFix(int num) {
        return new String(readByteFix(num));
    }


    public boolean hasNext() {
        return position < length;
    }
}
