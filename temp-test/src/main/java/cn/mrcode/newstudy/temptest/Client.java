package cn.mrcode.newstudy.temptest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2017/12/25 16:26
 */
public class Client {
    public static void main(String[] args) {
        Storage st = new Storage();
        List<Thread> threads = IntStream.range(1, 10).mapToObj(i -> {
            if (i % 2 == 0) {
                return new Thread(new Consumer(st), "consumer " + i);
            } else {
                return new Thread(new Producer(st), "producer " + i);
            }
        }).filter(t -> {
            t.start();
            return true;
        }).collect(Collectors.toList());
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {

            }
        });
    }
}
