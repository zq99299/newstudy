package cn.mrcode.newstudy.design.pattern.creational.singleton;

import org.junit.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * ${todo}
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/9/28 22:21
 */
public class SingletonTest {
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

    // 恶汉式 反射防御原理以及解决
    @Test
    public void test2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<HungrySingleton> objectClass = HungrySingleton.class;
        Constructor<HungrySingleton> constructor = objectClass.getDeclaredConstructor();
        HungrySingleton instance = HungrySingleton.getInstance();
        // java.lang.IllegalAccessException: Class cn.mrcode.newstudy.design.pattern.creational.singleton.HungrySingletonTest can not access a member of class cn.mrcode.newstudy.design.pattern.creational.singleton.HungrySingleton with modifiers "private"
        // 解决私有构造不能访问的限制
        constructor.setAccessible(true);
        HungrySingleton objectInstance = constructor.newInstance();


        System.out.println(instance);
        System.out.println(objectInstance);
        System.out.println(instance == objectInstance);
    }

    // 恶汉式
    @Test
    public void test3() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        StaticInnerClassSingleton instance = StaticInnerClassSingleton.getInstance();
        System.out.println(instance);

        Class<StaticInnerClassSingleton> objClass = StaticInnerClassSingleton.class;
        Constructor<StaticInnerClassSingleton> objConstructor = objClass.getDeclaredConstructor();
        objConstructor.setAccessible(true);
        StaticInnerClassSingleton objInstance = objConstructor.newInstance();
        System.out.println(objInstance);
        System.out.println(instance == objInstance);
    }

    // 懒汉模式下的反射防御攻击演示
    @Test
    public void test4() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        LazySingleton instance = LazySingleton.getInstance();
        System.out.println(instance);

        Class<LazySingleton> lazySingletonClass = LazySingleton.class;
        Constructor<LazySingleton> declaredConstructor = lazySingletonClass.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        LazySingleton lazySingleton = declaredConstructor.newInstance();
        System.out.println(lazySingleton);
        System.out.println(instance == lazySingleton);
    }

    // 懒汉模式下的反射防御攻击 加强版
    @Test
    public void test5() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        LazySingleton instance = LazySingleton.getInstance();
        System.out.println(instance);

        Class<LazySingleton> lazySingletonClass = LazySingleton.class;

        // 只是增加了这一段代码，把标志变量修改了
        Field flag = lazySingletonClass.getDeclaredField("flag");
        flag.setAccessible(true);
        flag.set(instance, true);

        Constructor<LazySingleton> declaredConstructor = lazySingletonClass.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        LazySingleton lazySingleton = declaredConstructor.newInstance();
        System.out.println(lazySingleton);
        System.out.println(instance == lazySingleton);
    }

    // 枚举单例模式序列化攻击测试
    @Test
    public void test6() throws IOException, ClassNotFoundException {
        EnumInstance instance = EnumInstance.getInstance();
        instance.setData(new Date());

        // 序列化到文件
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("EnumInstance"));
        oos.writeObject(instance);

        // 从文件读取出来

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("EnumInstance"));
        EnumInstance instanceFromFile = (EnumInstance) ois.readObject();

        System.out.println(instance);
        System.out.println(instanceFromFile);
        System.out.println(instance == instanceFromFile);
        System.out.println("===== 查看实例里面的 变量是否是同一个");
        System.out.println(instance.getData());
        System.out.println(instanceFromFile.getData());
        System.out.println(instance.getData() == instanceFromFile.getData());

    }

    // 枚举反射攻击
    @Test
    public void test7() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        EnumInstance instance = EnumInstance.getInstance();
        System.out.println(instance);

        Class<EnumInstance> enumInstanceClass = EnumInstance.class;

        Constructor<EnumInstance> declaredConstructor = enumInstanceClass.getDeclaredConstructor(String.class, int.class);
        declaredConstructor.setAccessible(true);
        EnumInstance enumInstance = declaredConstructor.newInstance("INSTANCE", 0);
        System.out.println(enumInstance);
        System.out.println(instance == enumInstance);
    }

    @Test
    public void test8() {
        EnumInstance instance = EnumInstance.INSTANCE;
        instance.print();
    }

    @Test
    public void test9() {
        System.out.println(Thread.currentThread().getName() + " ====== " + ThreadLocalInstance.getInstance());
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " ====== " + ThreadLocalInstance.getInstance());
            System.out.println(Thread.currentThread().getName() + " ====== " + ThreadLocalInstance.getInstance());
            System.out.println(Thread.currentThread().getName() + " ====== " + ThreadLocalInstance.getInstance());
        }).start();
        System.out.println(Thread.currentThread().getName() + " ====== " + ThreadLocalInstance.getInstance());
        System.out.println(Thread.currentThread().getName() + " ====== " + ThreadLocalInstance.getInstance());
        System.out.println(Thread.currentThread().getName() + " ====== " + ThreadLocalInstance.getInstance());
    }
}