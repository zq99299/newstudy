package cn.mrcode.newstudy.hpbase._10;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/22 23:20
 */
public class ChatRespones {
    // 响应类型
    private byte type;
    private boolean success; // 处理是否成功
    private String error; // 错误信息
    private String from;  // 来自谁
    private String info;  // 信息

    public ChatRespones() {
    }

    public ChatRespones(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
