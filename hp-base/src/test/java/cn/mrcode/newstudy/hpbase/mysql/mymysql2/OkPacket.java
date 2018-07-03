package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

import java.nio.ByteBuffer;

/**
 * ok 包
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/3 15:53
 */
public class OkPacket extends MySQLPacket {
    private byte header;
    private int affectedRows;
    private int lastInsertId;
    private int statusFlags;
    private int warnings;

    /*
    INT <1>	header	[00]或[fe]OK包头
    INT <lenenc>	affected_rows	受影响的行
    INT <lenenc>	last_insert_id	最后一个插入标识
    如果能力＆ CLIENT_PROTOCOL_41 {
      INT <2>	status_flags	状态标志
      INT <2>	warnings	警告的数量
     */
    //  常规空包 ok包
    public static ByteBuffer build(int sqlid) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.position(3);
        buffer.put((byte) (sqlid + 1));
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        BufferUtil.writeUB2(buffer, 2);
        BufferUtil.writeUB2(buffer, 0);
        int position = buffer.position();
        buffer.put(0, (byte) (position - 4));
        return buffer;
    }

    public byte getHeader() {
        return header;
    }

    public void setHeader(byte header) {
        this.header = header;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    public int getLastInsertId() {
        return lastInsertId;
    }

    public void setLastInsertId(int lastInsertId) {
        this.lastInsertId = lastInsertId;
    }

    public int getStatusFlags() {
        return statusFlags;
    }

    public void setStatusFlags(int statusFlags) {
        this.statusFlags = statusFlags;
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }
}
