package cn.mrcode.newstudy.hpbase.mysql.mymysql2.frontend;

import cn.mrcode.newstudy.hpbase.mysql.mymysql2.*;

import java.nio.ByteBuffer;

/**
 * 前段认证处理
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/3 12:33
 */
public class FrontendAuthHandler implements NIOHandler {
    private byte[] seed;  // 认证数据
    private FrontendConnection frontendConnection;

    public FrontendAuthHandler(FrontendConnection frontendConnection) {
        this.frontendConnection = frontendConnection;
    }

    @Override
    public void handler(byte[] data) {
        /**接收验证包，就是之前后端构造验证包发送给mysql服务器的 协议
         * {@link HandshakeResponse41#write(java.nio.ByteBuffer, byte)} */

        // 这里先直接返回一个ok包
        ByteBuffer ok = OkPacket.build(1);
        ok.flip();
        frontendConnection.write(ok);
        // 验证成功，切换处理器
        frontendConnection.setHandler(new FrontendConnectionHandler(frontendConnection));
    }

    public void sendHandshakeV10() {
        // 生成认证数据
        byte[] rand1 = "abcdefgh".getBytes();
        byte[] rand2 = "123456789abc".getBytes();

        // 保存认证数据
        byte[] seed = new byte[rand1.length + rand2.length];
        System.arraycopy(rand1, 0, seed, 0, rand1.length);
        System.arraycopy(rand2, 0, seed, rand1.length, rand2.length);
        this.seed = seed;

        HandshakeV10 h = new HandshakeV10();
        h.setProtocolVersion((byte) 10);
        h.setServerVersion("myMycat 0.1");
        h.setConnectionId(Thread.currentThread().getId());
        h.setAuthPluginDataPart1(rand1);
        h.setFiller((byte) 0);
        h.setCapabilityFlag(MySqlConnect.getClientCapabilities());
        // 8  0x08 latin1_swedish_ci
        h.setCharacterSet((byte) 8);
        h.setStatusFlags(2);
        h.setAuthPluginDataLen((byte) 21);
        h.setReserved(new byte[10]);
        h.setAuthPluginDataPart2(rand2);
        h.setAuthPluginName("mysql_native_password");
        ByteBuffer buffer = h.write();
        buffer.flip();
        frontendConnection.write(buffer);
    }
}
