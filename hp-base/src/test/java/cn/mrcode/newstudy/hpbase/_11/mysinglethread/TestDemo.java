package cn.mrcode.newstudy.hpbase._11.mysinglethread;

import java.io.IOException;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/7 22:16
 */
public class TestDemo {
    public static void main(String[] args) throws IOException {
        new NioReactor(9000).start();
    }
}
