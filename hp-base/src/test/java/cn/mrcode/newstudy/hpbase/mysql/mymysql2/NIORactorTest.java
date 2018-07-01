package cn.mrcode.newstudy.hpbase.mysql.mymysql2;

import org.testng.annotations.Test;

/**
 * ${desc}
 * @author zhuqiang
 * @version 1.0.1 2018/6/28 17:09
 * @date 2018/6/28 17:09
 * @since 1.0
 */
public class NIORactorTest {

    @Test
    public void testRun() throws Exception {
        NIORactor nioRactor = new NIORactor();
        nioRactor.start();
        nioRactor.register("localhost", 3306, "root", "123456", "mycat_dev_test_1");
        nioRactor.join();
    }
}