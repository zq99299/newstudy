package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

import java.io.IOException;

/**
 * 连接接口
 * 简单实现功能：为了 前后连接使用同一个ractor
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/3 12:23
 */
public interface SqlConnect {
    void read() throws IOException;
    void checkWrites() throws IOException;
}
