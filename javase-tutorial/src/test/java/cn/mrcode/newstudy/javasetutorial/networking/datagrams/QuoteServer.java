package cn.mrcode.newstudy.javasetutorial.networking.datagrams;

import java.io.IOException;

/**
 * <pre>
 * ${desc}
 * </pre>
 *
 * @author zhuqiang
 * @date 2019/3/29 15:58
 */
public class QuoteServer {
    public static void main(String[] args) throws IOException {
        new QuoteServerThread().start();
    }
}
