package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * 认证处理器
 * @author zhuqiang
 * @date 2018/6/28 14:03
 */
public class MySqlAuthHandler implements NIOHandler {
    private Logger log = LoggerFactory.getLogger(getClass());
    private MySqlConnect connect;

    public MySqlAuthHandler(MySqlConnect connect) {
        this.connect = connect;
    }

    @Override
    public void handler(byte[] data) {
        // 根据包类型 进行处理
        switch (data[4]) {
            case 0x00: //ok 包 v5.7 包长度 > 7
                log.info("ok 包 ： 认证成功，切换到MySqlConnectHandler");
                MySqlConnectHandler handler = new MySqlConnectHandler(connect);
                connect.setHandler(handler);
                connect.execSQL("select user()");
                break;
            case (byte) 0xFF: // err包
                log.info("err 包");
                break;
            case (byte) 0xFE: // eof包 v5.7 包长度 < 9
                log.info("eof 包");
                break;
            case 10: // 握手协议包
                log.info("握手包");
                HandshakeV10 handshakeV10 = buildHandShakePacketV10(data);
                HandshakeResponse41 response41 = buildHandshakeResponse41(handshakeV10);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                response41.write(buffer);
                buffer.flip();
                connect.write(buffer);
                break;
            default:
                log.error("未知包 type = {}", data[4]);
                break;
        }
    }


    private HandshakeV10 buildHandShakePacketV10(byte[] data) {
        HandshakeV10 packet = new HandshakeV10();
        packet.read(data);
        return packet;
    }

    private HandshakeResponse41 buildHandshakeResponse41(HandshakeV10 handshakeV10) {
        HandshakeResponse41 response41 = new HandshakeResponse41(handshakeV10);
        response41.setUser(connect.getUser());
        response41.setPasswd(connect.getPasswd());
        response41.setSchema(connect.getSchema());
        return response41;
    }
}
