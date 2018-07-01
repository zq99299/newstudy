package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

/**
 * 错误包
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/1 16:38
 */
public class ErrPacket extends MySQLPacket {
    private byte header;
    private int errorCode;
    private String sqlStateMarker;
    private String sqlState;
    private String errorMessage;

    /**
     * Type	Name	Description
     * int<1>	header	[ff] header of the ERR packet
     * int<2>	error_code	error-code
     * if capabilities & CLIENT_PROTOCOL_41 {
     * string[1]	sql_state_marker	# marker of the SQL State
     * string[5]	sql_state	SQL State
     * }
     * string<EOF>	error_message	human readable error message
     */
    public static ErrPacket builder(byte[] data) {
        MySQLMessage msm = new MySQLMessage(data);
        ErrPacket errPacket = new ErrPacket();
        errPacket.payloadLength = msm.readUB3();
        errPacket.sequenceId = (byte) msm.readUB1();
        errPacket.header = (byte) msm.readUB1();
        errPacket.errorCode = msm.readUB2();
        errPacket.sqlStateMarker = msm.readStringFix(1);
        errPacket.sqlState = msm.readStringFix(5);
        errPacket.errorMessage = msm.readStringWithEOFByRemaining();
        return errPacket;
    }

    public byte getHeader() {
        return header;
    }

    public void setHeader(byte header) {
        this.header = header;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getSqlStateMarker() {
        return sqlStateMarker;
    }

    public void setSqlStateMarker(String sqlStateMarker) {
        this.sqlStateMarker = sqlStateMarker;
    }

    public String getSqlState() {
        return sqlState;
    }

    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
