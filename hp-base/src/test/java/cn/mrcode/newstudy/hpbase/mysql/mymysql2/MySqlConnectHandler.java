package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

/**
 * 正常的数据交互处理
 * @author zhuqiang
 * @date 2018/6/28 14:05
 */
public class MySqlConnectHandler implements NIOHandler {
    public MySqlConnectHandler(MySqlConnect connect) {

    }

    @Override
    public void handler(byte[] data) {
        // 响应包的结构大致如下：
        // 第一个包：列个数；从sql中select xxx,xxx from 中的列个数
        // 第二部分：列定义，每个列定义为一个包
        // 第三部分：列定义后面跟随一个eof包
        // 第四部分：查询结果返回的值，顺序与返回的列定义一致；结果暂时看只有一个包，不排除大于16m 会分多个包的可能
        // 第五部分：结果后面跟随一个eof包
        System.out.println(" . " + data.length);
    }
}
