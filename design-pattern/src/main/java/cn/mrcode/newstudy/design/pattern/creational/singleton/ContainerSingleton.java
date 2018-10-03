package cn.mrcode.newstudy.design.pattern.creational.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/10/3 21:25
 */
public class ContainerSingleton {
    private static Map<String, Object> singletonMap = new HashMap<>();

    private ContainerSingleton() {
    }

    public static void putInstance(String key, Object instance) {
        if (!singletonMap.containsKey(key)) {
            singletonMap.put(key, instance);
        }
    }

    public static Object getInstance(String key) {
        return singletonMap.get(key);
    }
}
