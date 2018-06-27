package cn.mrcode.newstudy.hpbase.mysql.mymysql;

import cn.mrcode.newstudy.hpbase.mysql.Capabilities;
import cn.mrcode.newstudy.hpbase.mysql.SecurityUtil;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

/**
 * 客户端 -> 服务端的认证包
 * https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::HandshakeResponse
 * capabilities ： 能力集 https://dev.mysql.com/doc/internals/en/capability-flags.html#packet-Protocol::CapabilityFlags
 * <pre>
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/26 11:35
 * @date 2018/6/26 11:35
 * @since 1.0.0
 */
public class HandshakeResponse41 {
    private HandshakeV10 handshakeV10;
    public static final byte[] RESERVED = new byte[23];

    public HandshakeResponse41(HandshakeV10 handshakeV10) {
        this.handshakeV10 = handshakeV10;
    }

    /**
     * 获取客户端支持的能力集：
     * | 相加
     * &  if(能力集 & 能力) == 能力 就说明支持这个能力
     * @return
     */
    public static int getClientCapabilities() {
        int flag = 0;
        flag |= Capabilities.CLIENT_LONG_PASSWORD;
        flag |= Capabilities.CLIENT_FOUND_ROWS;
        flag |= Capabilities.CLIENT_LONG_FLAG;
        flag |= Capabilities.CLIENT_CONNECT_WITH_DB;
        flag |= Capabilities.CLIENT_ODBC;
        flag |= Capabilities.CLIENT_IGNORE_SPACE;
        flag |= Capabilities.CLIENT_PROTOCOL_41;
        flag |= Capabilities.CLIENT_INTERACTIVE;
        flag |= Capabilities.CLIENT_IGNORE_SIGPIPE;
        flag |= Capabilities.CLIENT_TRANSACTIONS;
        flag |= Capabilities.CLIENT_SECURE_CONNECTION;
        flag |= Capabilities.CLIENT_PLUGIN_AUTH;
        return flag;
    }

    public ByteBuffer builderResponse() {
        ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);
        buffer.position(3);
        buffer.put((byte) (handshakeV10.sequenceId + 1));
        // 4              capability flags   能力标识
        int clientCapabilities = getClientCapabilities();
        BufferUtil.writeUB4(buffer, clientCapabilities);
        // 4              max-packet size
        BufferUtil.writeUB4(buffer, 16 * 1024 * 1024);
        // 1              character set
        buffer.put(handshakeV10.getCharacterSet());
        // string[23]     reserved (all [0])  占位符
        buffer.put(RESERVED);
        // string[NUL]    username
        BufferUtil.writeStringWithNull(buffer, "root");

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

        byte[] password = password("123456");
        buffer.put((byte) password.length);
        BufferUtil.writeStringFix(buffer, password);

        /**
         if capabilities & CLIENT_CONNECT_WITH_DB {
         string[NUL]    database
         }
         */
        if ((clientCapabilities & Capabilities.CLIENT_CONNECT_WITH_DB) == Capabilities.CLIENT_CONNECT_WITH_DB) {
            BufferUtil.writeStringWithNull(buffer, "mycat_dev_test_1");
        } else {
            System.out.println("没有写入database");
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
        return buffer;
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
}
