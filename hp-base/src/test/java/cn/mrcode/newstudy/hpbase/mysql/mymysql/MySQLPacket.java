package cn.mrcode.newstudy.hpbase.mysql.mymysql;

/**
 * 通用包头
 * <pre>
 *
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date 2018/6/26 11:27
 */
public class MySQLPacket {
    // 包长度;占用3个字节
    protected int payloadLength;
    // 序列号
    protected byte sequenceId;

    /** 未压缩通用包头 ↑↑↑↑↑↑↑↑↑ */

    public int getPayloadLength() {
        return payloadLength;
    }

    public byte getSequenceId() {
        return sequenceId;
    }
}
