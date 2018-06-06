package cn.mrcode.newstudy.hpbase._11.singlethread;

import java.io.IOException;

public class MainServer {

    public static void main(String[] args) throws IOException {
        NIORector reacot = new NIORector();
        reacot.start(9000);
    }
}
