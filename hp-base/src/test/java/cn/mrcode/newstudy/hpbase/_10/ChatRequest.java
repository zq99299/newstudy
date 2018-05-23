package cn.mrcode.newstudy.hpbase._10;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/22 23:20
 */
public class ChatRequest {
    /* 登录 */
    public final static byte TYPE_LOGIN = 1;
    /* 群聊 */
    public final static byte TYPE_ROOM = 2;
    /* 私聊 */
    public final static byte TYPE_PRIVATE = 3;
    private byte type;
    private String user;
    private String to;
    private String from;
    private String info;

    public ChatRequest() {
    }

    public ChatRequest(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
