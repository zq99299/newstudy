package cn.mrcode.newstudy.hpbase.mysql.mymysql2.frontend;

import cn.mrcode.newstudy.hpbase.mysql.mymysql2.MySQLMessage;
import cn.mrcode.newstudy.hpbase.mysql.mymysql2.NIOHandler;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 数据处理
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/3 16:06
 */
public class FrontendConnectionHandler implements NIOHandler {
    private FrontendConnection frontendConnection;

    public FrontendConnectionHandler(FrontendConnection frontendConnection) {
        this.frontendConnection = frontendConnection;
    }

    @Override
    public void handler(byte[] data) {
        System.out.println("接收到一个查询");
        switch (data[4] & 0xFF) {
            case 3:
                MySQLMessage msm = new MySQLMessage(data);
                msm.readUB3();
                msm.read();
                msm.read();
                String s = msm.readStringWithEOFByRemaining();
                frontendConnection.getMySqlConnect().execSQL(s);
                frontendConnection.getMySqlConnect().setResponseHandler(new ResponseHandler() {

                    @Override
                    public void handl(List<byte[]> queryRes) {
                        ByteBuffer res = ByteBuffer.allocate(1024 * 16 * 2);
                        for (byte[] queryRe : queryRes) {
                            res.put(queryRe);
                        }
                        res.flip();
                        frontendConnection.write(res);
                    }
                });
                break;
        }
    }
}
