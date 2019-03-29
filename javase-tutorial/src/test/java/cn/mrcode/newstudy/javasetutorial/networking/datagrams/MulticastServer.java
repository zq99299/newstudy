package cn.mrcode.newstudy.javasetutorial.networking.datagrams;

/**
 * <pre>
 * ${desc}
 * </pre>
 *
 * @author zhuqiang
 * @date 2019/3/29 16:22
 */
public class MulticastServer {
    public static void main(String[] args) throws java.io.IOException {
        new MulticastServerThread().start();
    }
}
