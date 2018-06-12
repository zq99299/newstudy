package cn.mrcode.newstudy.javasetutorial.networking;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/12     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/12 11:25
 * @date 2018/6/12 11:25
 * @since 1.0.0
 */
public class TestDemo {
    @Test
    public void fun1() {
        try {
            URL myURL = new URL("http://example.com/");
            URLConnection myURLConnection = myURL.openConnection();
            myURLConnection.connect();
        } catch (MalformedURLException e) {
            // new URL() failed
            // ...
        } catch (IOException e) {
            // openConnection() failed
            // ...
        }
    }

    // 从链接中获取输入流
    @Test
    public void fun2() throws IOException {
        URL oracle = new URL("https://www.oracle.com/index.html");
        URLConnection yc = oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }

    // 模拟登陆；带上cookie
    @Test
    public void fun3() throws IOException {
        CookieManager manager = new CookieManager();
        //设置cookie策略，只接受与你对话服务器的cookie，而不接收Internet上其它服务器发送的cookie
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(manager);

        // 登录
        URL root = new URL("http://localhost:9114");
        URL signin = new URL(root, "/api/account/signin");
        URLConnection signinConnection = signin.openConnection();
        signinConnection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(
                signinConnection.getOutputStream());
        out.write("account=zhuqiang@tidebuy.net&pwd=123456");
        out.close();
        // 根据测试 好像在调用了获取输入流的时候 请求才发出去的？
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        signinConnection.getInputStream()));
        String decodedString;
        while ((decodedString = in.readLine()) != null) {
            System.out.println(decodedString);
        }
        in.close();

        // 使用了CookieManager之后，这个再获取的时候就获取不到值了
//        String cookie = signinConnection.getHeaderField("Set-Cookie");
//        System.out.println(cookie);
        URL ugdDetail = new URL(root, "/api/ugd/20");
        URLConnection ugdDetailConnection = ugdDetail.openConnection();
//        ugdDetailConnection.addRequestProperty("Cookie", cookie.split(";")[0].trim());
        BufferedReader ugdDetailIn = new BufferedReader(
                new InputStreamReader(
                        ugdDetail.openStream()));
        String ugdDetailDecodedString;
        while ((ugdDetailDecodedString = ugdDetailIn.readLine()) != null) {
            System.out.println(ugdDetailDecodedString);
        }
    }
}
