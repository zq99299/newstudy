package cn.mrcode.newstudy.hpbase._04.mycat2;

import org.junit.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.IntStream;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/30 21:36
 */
public class TestDemo {

    // 主要对 flatMap 展开研究
    @Test
    public void fun1() {
        List<List<Integer>> lists = new ArrayList<>(3);
        lists.add(randomList());
        lists.add(randomList());
        lists.add(randomList());
        long count = lists
                .stream()
                .flatMap(f -> {
                    // [0, 1, 2, 3, 4] 多次被打印
                    System.out.println(f);
                    return f.stream();
                })
                .filter(f -> f > 0)
                .count();
        System.out.println(count);
    }

    private List<Integer> randomList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        return list;
    }

    // 主要对 Option的研究
    @Test
    public void fun2() {
        List<Integer> arrs = Arrays.asList(1, 1, 2, 3, 4, 5, 5);
        List<Integer> empty = new ArrayList<>();
        Integer integer =
//                empty   // 99
                arrs    // 1
                        .stream()
                        .findFirst().orElse(99);
        System.out.println(integer);
    }

    @Test
    public void fun3() {
        // 包括0-9
        IntStream.rangeClosed('0', '9').forEach(c -> System.out.println(c));
        // 不包括9
        IntStream.range('0', '9').forEach(c -> System.out.println(c));

        // 48 49 50 51 52 53 54 55 56
        for (int i = '0'; i < '9'; i++) {
            System.out.print(i + " ");
        }
    }

    // 使用 Collector 收集器来实现统计功能
    @Test
    public void fun4() {
        List<Integer> arrs = Arrays.asList(1, 1, 2, 3, 4, 5, 5);
        Long collect = arrs.stream()
                .collect(new My(2));
        System.out.println(collect);
    }

    class My implements Collector<Integer, Integer, Long> {
        private Long count = 0L;
        private Integer limit;

        public My(Integer limit) {
            this.limit = limit;
        }

        @Override
        public Supplier<Integer> supplier() {
            return () -> Integer.valueOf(0);
        }

        @Override
        public BiConsumer<Integer, Integer> accumulator() {
            return (a1, a2) -> {
                if (count++ < limit) {
                    count++;
                }
            };
        }

        @Override
        public BinaryOperator<Integer> combiner() {
            return null;
        }

        @Override
        public Function<Integer, Long> finisher() {
            return i -> {
                return count;
            };
        }

        @Override
        public Set<Characteristics> characteristics() {
            return new HashSet<>();
        }
    }

    @Test
    public void fun5() {
        Function<String, String> addHeader = Letter::addHeader;
        Function<String, String> transformationPipeline
                =
                addHeader.andThen(Letter::checkSpelling)
                .andThen(Letter::addFooter);
        System.out.println(transformationPipeline.apply("xxxx"));
    }

    public static class Letter {
        public static String addHeader(String text) {
            return "From Raoul, Mario and Alan: " + text;
        }

        public static String addFooter(String text) {
            return text + " Kind regards";
        }

        public static String checkSpelling(String text) {
            return text.replaceAll("labda", "lambda");
        }
    }
}
