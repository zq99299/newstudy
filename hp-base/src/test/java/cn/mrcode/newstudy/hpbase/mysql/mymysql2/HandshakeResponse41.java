package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

import cn.mrcode.newstudy.hpbase.mysql.Capabilities;
import cn.mrcode.newstudy.hpbase.mysql.SecurityUtil;
import cn.mrcode.newstudy.hpbase.mysql.mymysql.BufferUtil;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/28 23:15
 */
public class HandshakeResponse41 {
    private HandshakeV10 handshakeV10;
    private String user;
    private String passwd;
    private String schema;
    // 保留字段
    public static final byte[] RESERVED = new byte[23];

    public HandshakeResponse41(HandshakeV10 handshakeV10) {
        this.handshakeV10 = handshakeV10;
    }

    public void write(ByteBuffer buffer, byte charetSetIndex) {
        buffer.position(3);
        buffer.put((byte) (handshakeV10.sequenceId + 1));
        // 4              capability flags   能力标识
        int clientCapabilities = MySqlConnect.getClientCapabilities();

        BufferUtil.writeUB4(buffer, clientCapabilities);
        // 4              max-packet size
        BufferUtil.writeUB4(buffer, MySqlConnect.DEFAULT_PAKET_SIZE);
        // 1              character set
        buffer.put(charetSetIndex);  // 使用的 字符集是在登录认证的时候 指定的，否则后续的数据就会乱码
        // string[23]     reserved (all [0])  占位符
        buffer.put(RESERVED);
        // string[NUL]    username
        BufferUtil.writeStringWithNull(buffer, user);

        /**
         if capabilities & CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA {
         lenenc-int     length of auth-response
         string[n]      auth-response
         } else if capabilities & CLIENT_SECURE_CONNECTION {
         1              length of auth-response
         string[n]      auth-response
         } else {
         string[NUL]    auth-response
         }
         */
        if ((clientCapabilities & Capabilities.CLIENT_SECURE_CONNECTION) != Capabilities.CLIENT_SECURE_CONNECTION) {
            throw new RuntimeException("不支持scramble411加密");
        }

        byte[] password = password(passwd);
        buffer.put((byte) password.length);
        BufferUtil.writeStringFix(buffer, password);

        /**
         if capabilities & CLIENT_CONNECT_WITH_DB {
         string[NUL]    database
         }
         */
        if ((clientCapabilities & Capabilities.CLIENT_CONNECT_WITH_DB) == Capabilities.CLIENT_CONNECT_WITH_DB) {
            BufferUtil.writeStringWithNull(buffer, schema);
        } else {
            System.err.println("没有写入database");
        }


        /**
         if capabilities & CLIENT_PLUGIN_AUTH {
         string[NUL]    auth plugin name
         }
         */
        if ((clientCapabilities & Capabilities.CLIENT_PLUGIN_AUTH) == Capabilities.CLIENT_PLUGIN_AUTH) {
            BufferUtil.writeStringWithNull(buffer, handshakeV10.getAuthPluginName());
        }
        int position = buffer.position();
        buffer.put(0, (byte) (position - 4));
    }

    /**
     * 密码加密：
     * https://dev.mysql.com/doc/internals/en/secure-password-authentication.html
     * 需要客户端能力：CLIENT_SECURE_CONNECTION
     * @param s
     * @return
     * @throws NoSuchAlgorithmException
     */
    private byte[] password(String s) {
        byte[] passwd = s.getBytes();
        byte[] authPluginDataPart1 = handshakeV10.getAuthPluginDataPart1();
        int s1 = authPluginDataPart1.length;
        byte[] authPluginDataPart2 = handshakeV10.getAuthPluginDataPart2();
        int s2 = authPluginDataPart2.length;
        byte[] seed = new byte[s1 + s2];
        System.arraycopy(authPluginDataPart1, 0, seed, 0, s1);
        System.arraycopy(authPluginDataPart2, 0, seed, s1, s2);

        // 加密算法
        // SHA1( password ) XOR SHA1( "20-bytes random data from server" <concat> SHA1( SHA1( password ) ) )
        // 20 字节的随机字符 来自服务器；也就是这里的authPluginDataPart1 + authPluginDataPart2
        try {
            return SecurityUtil.scramble411(passwd, seed);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
