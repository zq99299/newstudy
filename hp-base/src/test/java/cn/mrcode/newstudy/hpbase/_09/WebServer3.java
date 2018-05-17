package cn.mrcode.newstudy.hpbase._09;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/05/04     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/5/4 17:31
 * @date 2018/5/4 17:31
 * @since 1.0.0
 */
public class WebServer3 {
    @Test
    public void fun1(){
        System.out.println('\r');
    }
    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(80);
        while (true) {
            Socket accept = serverSocket.accept();
            executor.execute(() -> {
                try {
                    LineNumberReader in = new LineNumberReader(new InputStreamReader(accept.getInputStream()));
                    String lin = null;
                    while ((lin = in.readLine()) != null) {
                        System.out.println(lin);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}