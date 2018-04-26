package cn.mrcode.newstudy.hpbase._07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ExecutorDemo {
    public static void main(String[] args) throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        List<Future<Long>> collect = Files.list(Paths.get("D:/"))
                .filter(i -> !i.toFile().isDirectory()) // 过滤掉文件夹
                .map(file -> (Callable<Long>) () -> Files.size(file)) // 统计所有文件的大小
                .map(c -> es.submit(c))
                .collect(Collectors.toList());
        Supplier<LongStream> ls = () -> (collect
                .stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return -1L;
                }).mapToLong(i -> i));
        ls.get().forEach(System.out::println);
        System.out.println(ls.get().sum());
        es.shutdown();
    }
}
