package cn.mrcode.newstudy.design.pattern.creational.prototype;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;


/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/19 22:00
 */
public class HungrySingletonTest {
    @Test
    public void fun1() throws CloneNotSupportedException {
        HungrySingleton i1 = HungrySingleton.getInstance();
        HungrySingleton i2 = (HungrySingleton) i1.clone();
        System.out.println(i1);
        System.out.println(i2);
    }

    @Test
    public void fun2() throws CloneNotSupportedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        HungrySingleton i1 = HungrySingleton.getInstance();
        // 实现克隆方法后，权限修饰符最小为 protected，所以不能防止被调用
        // 假设是 private 了，那么也可以使用反射进行调用 clone 方法
        Method clone = i1.getClass().getDeclaredMethod("clone");
        clone.setAccessible(true);
        HungrySingleton i2 = (HungrySingleton) clone.invoke(i1);
        System.out.println(i1);
        System.out.println(i2);
    }

    @Test
    public void fun3() {
        HashMap<String, Date> map = new HashMap<>();
        map.put("1", new Date());
        Date date = new Date();
        date.setTime(6666666L);
        map.put("2", date);

        System.out.println(map);
        HashMap<String, Date> clone = (HashMap<String, Date>) map.clone();
        System.out.println(clone);
    }
}