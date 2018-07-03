package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

import cn.mrcode.newstudy.hpbase.mysql.mymysql2.protocol.text.ComQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 正常的数据交互处理
 * @author zhuqiang
 * @date 2018/6/28 14:05
 */
public class MySqlConnectHandler implements NIOHandler {
    private int state = 0;
    private ComQueryResponse response = null;
    private Logger log = LoggerFactory.getLogger(getClass());
    private MySqlConnect connect;

    public MySqlConnectHandler(MySqlConnect connect) {
        this.connect = connect;
    }

    @Override
    public void handler(byte[] data) {
        // 响应包的结构大致如下：
        // 第一个包：列个数；从sql中select xxx,xxx from 中的列个数
        // 第二部分：列定义，每个列定义为一个包
        // 第三部分：列定义后面跟随一个eof包
        // 第四部分：返回值，一行一个包；行内字段顺序和前面的顺序一致
        // 第五部分：结果后面跟随一个eof包

        int flag = data[4] & 0xFF;
        if (flag == 0xFF) {
            response = new ComQueryResponse(ErrPacket.builder(data));
            response.addMysqlOriginalPacket(data);
            connect.response(response);
            state = 0;
        } else {
            switch (state) {
                case 0:  // 列个数
                    response = new ComQueryResponse((int) data[4]);
                    response.addMysqlOriginalPacket(data);
                    state = 1;
                    break;
                case 1: // 列定义
                    response.addMysqlOriginalPacket(data);
                    if (response.parseAndAddColumn(data)) {
                        state = 2;
                    }
                    break;
                case 2: // 行结果
                    response.addMysqlOriginalPacket(data);
                    if (response.parseAndAddRow(data)) {
                        connect.response(response);
                        state = 0;
                        response = null;
                    }
                    break;
                default:
                    throw new RuntimeException("异常");
            }
        }
        System.out.println(response);
    }
}
