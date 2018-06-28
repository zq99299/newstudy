package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/28     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/28 14:06
 * @date 2018/6/28 14:06
 * @since 1.0.0
 */
public interface NIOHandler {
    void handler(byte[] data);
}
