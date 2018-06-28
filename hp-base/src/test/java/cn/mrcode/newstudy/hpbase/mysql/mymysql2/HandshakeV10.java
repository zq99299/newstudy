package cn.mrcode.newstudy.hpbase.mysql.mymysql2;


import cn.mrcode.newstudy.hpbase.mysql.Capabilities;

/**
 * 服务端 -> 客户端
 * 握手包 v10版本 : https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::Handshake
 * int<n>类型的都是无符号整数；
 * <pre>
 * 1              [0a] protocol version
 * string[NUL]    server version
 * 4              connection id
 * string[8]      auth-plugin-data-part-1
 * 1              [00] filler
 * 2              capability flags (lower 2 bytes)
 * if more data in the packet:
 * 1              character set
 * 2              status flags
 * 2              capability flags (upper 2 bytes)
 * if capabilities & CLIENT_PLUGIN_AUTH {
 * 1              length of auth-plugin-data
 * } else {
 * 1              [00]
 * }
 * string[10]     reserved (all [00])
 * if capabilities & CLIENT_SECURE_CONNECTION {
 * string[$len]   auth-plugin-data-part-2 ($len=MAX(13, length of auth-plugin-data - 8))
 * if capabilities & CLIENT_PLUGIN_AUTH {
 * string[NUL]    auth-plugin name
 * }
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/24 21:43
 */
public class HandshakeV10 extends MySQLPacket {
    // 协议版本
    private byte protocolVersion;
    // 服务器版本 string[null]
//    private byte[] serverVersion;
    private String serverVersion;
    // 线程id？ 占用4个字节
    private long connectionId;
    // 占用8个字节
    private byte[] authPluginDataPart1;
    // 占用1个字节
    private byte filler;
    // 能力标识 lower 2 bytes
    private int capabilityFlags;
    // ------>  如果后面还有更多的数据则
    //
    private byte characterSet;
    // 状态标志 2 个字节
    private int statusFlags;
    // 能力标识2 upper 2 bytes
    private int capabilityFlags2;

    // 认证插件数据长度 1 个 字节
    private byte authPluginDataLen;
    // 保留字段 string[10] ; 目前全部为 0x00
    private byte[] reserved;

    // 插件名称
    // 如果authPluginDataLen不为 0x00;则规则为string[$len]
    // 但是 auth-plugin-data-part-2 ($len=MAX(13, length of authPluginDataLen - 8)
    // 否则规则为string[Nul]
    private String authPluginName;

    private byte[] authPluginDataPart2;

    public void read(byte[] data) {
        MySQLMessage msg = new MySQLMessage(data);
        this.payloadLength = msg.readUB3();
        this.sequenceId = msg.read();
        this.protocolVersion = msg.read();
        this.serverVersion = msg.readStringWithNull();
        this.connectionId = msg.readUB4();
        this.authPluginDataPart1 = msg.readByteFix(8);
        this.filler = msg.read();
        this.capabilityFlags = msg.readUB2();
        if (!msg.hasNext()) {
            return;
        }
        this.characterSet = msg.read();
        this.statusFlags = msg.readUB2();
        this.capabilityFlags2 = msg.readUB2();

        int clientCapabilities = MySqlConnect.getClientCapabilities();

        // if capabilities & CLIENT_PLUGIN_AUTH {
        // * 1              length of auth-plugin-data
        if ((clientCapabilities & Capabilities.CLIENT_PLUGIN_AUTH) == Capabilities.CLIENT_PLUGIN_AUTH) {
            this.authPluginDataLen = msg.read();
        } else {
            this.authPluginDataLen = msg.read();
        }

        this.reserved = msg.readByteFix(10);


        // * if capabilities & CLIENT_SECURE_CONNECTION {
        // * string[$len]   auth-plugin-data-part-2 ($len=MAX(13, length of auth-plugin-data - 8))
        if ((clientCapabilities & Capabilities.CLIENT_SECURE_CONNECTION) == Capabilities.CLIENT_SECURE_CONNECTION) {
            // 实际情况和文档有一点出入；21字节是没有问题，但是这这里的类型有问题，最后一个为0 是string.nul
            // 暂时不知道是什么原因
//            this.authPluginDataPart2 = msg.readByteFix(Integer.max(13, authPluginDataLen - 8));
            this.authPluginDataPart2 = msg.readBytesWithNull();
        } else {
            System.out.println("不支持 CLIENT_SECURE_CONNECTION");
        }

        // * if capabilities & CLIENT_PLUGIN_AUTH {
        // * string[NUL]    auth-plugin name
        // * }
        if ((clientCapabilities & Capabilities.CLIENT_PLUGIN_AUTH) == Capabilities.CLIENT_PLUGIN_AUTH) {
            this.authPluginName = msg.readStringWithNull();
        }
    }

    public byte getProtocolVersion() {
        return protocolVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public long getConnectionId() {
        return connectionId;
    }

    public byte[] getAuthPluginDataPart1() {
        return authPluginDataPart1;
    }

    public byte getFiller() {
        return filler;
    }

    public int getCapabilityFlags() {
        return capabilityFlags;
    }

    public byte getCharacterSet() {
        return characterSet;
    }

    public int getStatusFlags() {
        return statusFlags;
    }

    public int getCapabilityFlags2() {
        return capabilityFlags2;
    }

    public byte getAuthPluginDataLen() {
        return authPluginDataLen;
    }

    public byte[] getReserved() {
        return reserved;
    }

    public String getAuthPluginName() {
        return authPluginName;
    }

    public byte[] getAuthPluginDataPart2() {
        return authPluginDataPart2;
    }
}
