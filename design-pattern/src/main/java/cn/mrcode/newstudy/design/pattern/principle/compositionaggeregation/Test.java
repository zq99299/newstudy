package cn.mrcode.newstudy.design.pattern.principle.compositionaggeregation;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 20:53
 */
public class Test {
    public static void main(String[] args) {
        ProductDao productDao = new ProductDao();
        productDao.setDbConnection(new MysqlDBConnection());
//        productDao.setDbConnection(new PostgreesqlDBConnection());
        productDao.addProduct();
    }
}
