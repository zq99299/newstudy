package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

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

    public int readUB1() {
        return data[position++] & 0xFF;
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

    public String readStrWithLenenc() {
        Long length = readIntWithLenenc();
        if (length == null) return null;
        // todo 精度不会有问题吗？
        return readStringFix(length.intValue());
    }

    /**
     * 读取剩余字节为字符串
     * @return
     */
    public String readStringWithEOFByRemaining() {
        return new String(readByteFix(data.length - position));
    }

    public Long readIntWithLenenc() {
        int i = readUB1();
        long length = 0;
        if (i < 0xfb) {
            length = i;
        } else if (i == 0xFC) {
            length = readUB2();
        } else if (i == 0xFD) {
            length = readUB3();
        } else if (i == 0xFE) {
            length = readUB8();
        } else {
            return null;
        }
        return length;
    }

    private long readUB8() {
        final byte[] b = this.data;
        long i = (long) b[position++] & 0xff;
        i |= (long) (b[position++] & 0xff) << 8;
        i |= (long) (b[position++] & 0xff) << (8 * 2);
        i |= (long) (b[position++] & 0xff) << (8 * 3);
        i |= (long) (b[position++] & 0xff) << (8 * 4);
        i |= (long) (b[position++] & 0xff) << (8 * 5);
        i |= (long) (b[position++] & 0xff) << (8 * 6);
        i |= (long) (b[position++] & 0xff) << (8 * 7);
        i |= (long) (b[position++] & 0xff) << (8 * 8);
        return i;
    }
}
