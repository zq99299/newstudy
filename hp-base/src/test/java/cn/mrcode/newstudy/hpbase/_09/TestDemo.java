package cn.mrcode.newstudy.hpbase._09;

import org.junit.Test;

import java.net.*;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/13 21:37
 */
public class TestDemo {
    @Test
    public void fun1() throws UnknownHostException {
        InetAddress[] allByName = InetAddress.getAllByName("www.baidu.com");
        for (InetAddress inetAddress : allByName) {
            System.out.println(inetAddress);
        }

        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost);

        InetAddress byAddress = InetAddress.getByAddress(new byte[]{119, 75, (byte) 216, 20});
        System.out.println(byAddress);

        System.out.println(URLEncoder.encode("www.baidu.com/sss?xx=22&xx3kox=中国"));
    }

    @Test
    public void fun2() {
        String s = URLConnection.guessContentTypeFromName("/ss.mp3");
        System.out.println(s);
    }
}
