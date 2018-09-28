package cn.mrcode.newstudy.design.pattern.creational.builder;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;

import java.util.concurrent.TimeUnit;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/30 22:36
 */
public class Test {
    public static void main(String[] args) {
        DirectorBoss boss = new DirectorBoss();
        boss.setBuilder(new ActualBuilder());
        Computer computer = boss.createComputer(
                "酷睿I7",
                "华硕主板",
                "三星硬盘",
                "七彩虹显卡",
                "金河田电源",
                "万紫千红内存"
        );
        System.out.println(computer);

        ImmutableSet<String> build = ImmutableSet.<String>builder().add("a").add("b").build();
        // [a, b]
        System.out.println(build);

        Cache<Object, Object> cache = CacheBuilder.newBuilder()
                .initialCapacity(1000) // 初始容量
                .maximumSize(10000) // 最大容量，最近最少使用算法
                .expireAfterAccess(12, TimeUnit.HOURS) // 过期时间
                .concurrencyLevel(5) // 最大并发数，同一时间最多运行5个线程执行写入操作
                .build();

    }
}
