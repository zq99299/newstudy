package cn.mrcode.newstudy.design.pattern.principle.compositionaggeregation;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 21:08
 */
public class PostgreesqlDBConnection implements DBConnection {
    @Override
    public String getConnection() {
        return "使用 Postgreesql 数据库连接";
    }
}
