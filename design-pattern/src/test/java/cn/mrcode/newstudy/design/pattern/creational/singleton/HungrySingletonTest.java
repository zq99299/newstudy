package cn.mrcode.newstudy.design.pattern.creational.singleton;

import org.junit.Test;

import java.io.*;

/**
 * ${todo}
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/9/28 22:21
 */
public class HungrySingletonTest {
    @Test
    public void test() throws IOException, ClassNotFoundException {
        HungrySingleton instance = HungrySingleton.getInstance();


        // 序列化到文件
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("HungrySingleton"));
        oos.writeObject(instance);

        // 从文件读取出来

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("HungrySingleton"));
        HungrySingleton instanceFromFile = (HungrySingleton) ois.readObject();

        System.out.println(instance);
        System.out.println(instanceFromFile);
        System.out.println(instance == instanceFromFile);
    }
}