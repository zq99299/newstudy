package cn.mrcode.newstudy.hpbase.mysql.mymysql2.frontend;

import java.util.List;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/3 16:59
 */
public interface ResponseHandler {
    public void handl(List<byte[]> queryRes);
}
