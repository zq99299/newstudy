package cn.mrcode.newstudy.design.pattern.principle.compositionaggeregation;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 20:51
 */
public class ProductDao {
    private DBConnection dbConnection;

    public void addProduct() {
        String connection = dbConnection.getConnection();
        System.out.println("使用 " + connection + " 添加产品");
    }

    public void setDbConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}
