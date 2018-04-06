package cn.mrcode.newstudy.hpbase._04.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/1 16:02
 */
public class Demo {
    public List<Dish> mockdata() {
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH));
        return menu;
    }

    // 获取高热量的菜名
    @Test
    public void fun1() {
        mockdata()
                .stream()  // 获得流，下面是建立流水线
                .filter(d -> d.getCalories() > 300)
                .map(Dish::getName)
                .limit(3)
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }

    /**
     * 斐波纳契数列的一部分：0, 1, 1,2, 3, 5, 8, 13, 21, 34, 55…
     * 使用流来创建这个序列：
     * 斐波纳契数列解释：
     * 第1组：0，1
     * 第2组：0+1，1+1 = 1，2
     * 第3组：1+2,2+3 = 2,5
     * ....
     * 可以看出来，是一组一组的，后面一组的数，完全是根据前面一组的数得到的；
     * （n,x）-> (n+x,n+x+x)
     */
    @Test
    public void fun2() {
        Stream.iterate(new int[]{0, 1}, (t) -> new int[]{t[0] + t[1], t[0] + t[1] + t[1]})
                .limit(10)
                .forEach(ints -> System.out.println(Arrays.toString(ints)));

        Stream.iterate(new int[]{0, 1}, (t) -> new int[]{t[0] + t[1], t[0] + t[1] + t[1]})
                .limit(10)
                // 扁平成数值流
                .flatMapToInt(ints -> Arrays.stream(ints))
                .forEach(System.out::println);
    }

    /**
     * 斐波纳契数:一个一个的计算出来；
     */
    @Test
    public void fun3() {
        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1; // 记录当前的状态

            public int getAsInt() {
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };
        IntStream.generate(fib).limit(10).forEach(System.out::println);
    }

    @Test
    public void fun4() {
        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();
        List<Integer> numbers = stream.reduce(
                new ArrayList<Integer>(),
                (List<Integer> l, Integer e) -> {
                    l.add(e);
                    return l;
                },
                (List<Integer> l1, List<Integer> l2) -> {
                    l1.addAll(l2);
                    return l1;
                });
        System.out.println(numbers);
    }
}
