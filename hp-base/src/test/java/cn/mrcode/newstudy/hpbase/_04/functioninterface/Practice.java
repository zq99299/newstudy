package cn.mrcode.newstudy.hpbase._04.functioninterface;

import org.junit.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/19 22:08
 */
public class Practice {
    @Test
    public void consumerTest() {

        // 自定义一个函数接口，其实就是一个接口
        AppleInterface in = () -> System.out.println("x");
        in.test();

        // 使用系统预设的函数接口进行测试
        // consumer 用于接收一个值，然后执行accept操作；不返回值
        Consumer<Apple> c1 = (Apple app) -> {
            System.out.println(app.getColor() + "," + app.getWeight());
        };
        List<Apple> apps = Arrays.asList(
                new Apple("red", 120),
                new Apple("blue", 80),
                new Apple("green", 100));
        for (Apple app : apps) {
            c1.accept(app);
        }

        // consumer 组成链条
        Consumer<Apple> c2 = (Apple app) -> {
            System.out.println(app.getColor() + "....." + app.getWeight());
        };
        Consumer<Apple> c3 = c1.andThen(c2);
        for (Apple app : apps) {
            c3.accept(app);
        }

    }

    // Supplier<T>：该接口不接收任何参数，返回一个对象T
    @Test
    public void supplierTest() {
        Supplier<Apple> supplier = () -> new Apple("hello supplier", 999);
        Apple app = supplier.get();
        System.out.println(app.getColor() + "," + app.getWeight());

    }

    // Predicate<T>：该接口接收一个对象T，返回一个Boolean
    @Test
    public void predicateTest() {
        //系统预定义函数式接口测试
        Predicate<Apple> p1 = (Apple a) -> {
            if (a.getWeight() > 90) return true;
            return false;
        };
        Predicate<Apple> p2 = (Apple a) -> {
            if (a.getColor().equals("blue")) return true;
            return false;
        };

        List<Apple> apps = Arrays.asList(
                new Apple("red", 120),
                new Apple("blue", 80),
                new Apple("green", 100));

        filterApple(apps, p1);
        filterApple(apps, p2);

    }

    public void filterApple(List<Apple> apps, Predicate<Apple> p) {
        for (Apple app : apps) {
            if (p.test(app)) {
                System.out.println(app.getColor() + "," + app.getWeight());
            }
        }
    }

    // Function<T,R>: 该接口接收一个对象T，经过转换判断，返回一个对象R
    @Test
    public void functionTest() {
        List<Apple> apps = Arrays.asList(
                new Apple("red", 120),
                new Apple("blue", 80),
                new Apple("green", 100));
        Function<Apple, String> f = (app) -> app.getColor();

        apps.stream().map(f).forEach(System.out::println);

        // 经过这个测试用用例之后，再去看流api 应该会比较容易了
        // 其实本质还是 定义了几个功能接口，然后逻辑就是要你自己来实现这个功能接口，按照类型传递进去
    }

    //BiFunction<T,U,R> 输入 T,U  返回R
    @Test
    public void biFunctionTest() {
        List<Apple> apps = Arrays.asList(
                new Apple("red", 120),
                new Apple("blue", 80),
                new Apple("green", 100),
                new Apple("green", 10));
        BiFunction<Apple, Apple, Boolean> b = (a1, a2) -> a1.getColor() == a2.getColor();

        Boolean apply = b.apply(apps.get(2), apps.get(3));
        System.out.println(apply);
    }

    // UnaryOperator<T> 输入T,返回T; 用于给这个t加工处理
    @Test
    public void unaryOperatorTest() {
        UnaryOperator<Apple> uo = (app) -> {
            app.setColor(app.getColor() + "加工");
            return app;
        };
        Apple red = new Apple("red", 120);
        System.out.println(uo.apply(red));
    }

    /** ==============    stream api ===================== */

    // 无限流测试
    @Test
    public void generateTest() {
        Stream.generate(Math::random).forEach(System.out::println);
    }

    // 无限流测试
    @Test
    public void iterateTest() {
        // 第一个参数 是 初始值，第二个参数是 UnaryOperator 接口：接收一个T，返回一个T
        Stream.iterate(0, n -> n + 1).forEach(System.out::println);
    }

    // 将一个流中的数据转换到另一个流中
    // 流转换测试: 过滤
    @Test
    public void filterTest() {
        IntStream.range(0, 10)
                .filter(n -> n > 5)  // 只要大于5的值
                .forEach(System.out::println);
    }

    // 流转换测试
    @Test
    public void mapTest() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        Stream.of(arr)
                .map(n -> n + "_")  // 把int类型的转换为字符串： 数字流转换成字符串流
                .forEach(n -> {
                    System.out.println(n);
                });
    }

    // 流转换: 展开流
    @Test
    public void flatMapTest() {
        Stream<Integer> s1 = Stream.of(1, 2, 3, 4, 5);
        // 每个元素 都转换成了一个Integer流
        Stream<Stream<Integer>> arrs = s1.map(i -> Stream.iterate(1, n -> n + 1).limit(i));

//        arrs.forEach(System.out::println);  // 使用该方法打印的肯定是每个流的地址

        // 上一个流集合展开成一个流
        Stream<Integer> integerStream = arrs.flatMap(n -> n);
        integerStream.forEach(System.out::println);
    }

    // 提取子流
    @Test
    public void skipAndLimitTest() {
        Stream<Integer> s1 = Stream.of(1, 2, 3, 4, 5);
        s1.skip(3).forEach(System.out::println); // 跳过了3个元素，结果是 4,5

        s1 = Stream.of(1, 2, 3, 4, 5); // 流只能使用一次，用完就被关闭了
        s1.limit(3).forEach(System.out::println); // 只需要3个元素，结果是 1，2，3
    }

    // 组合流
    @Test
    public void concatTest() {
        Stream<Integer> s1 = Stream.of(1, 2, 3, 4, 5);
        Stream<Integer> s2 = Stream.of(6, 7, 8, 9, 10);

        // 把s1 和 s2 合成一个流
        Stream<Integer> s3 = Stream.concat(s1, s2);
        s3.forEach(System.out::println);
    }


    // 有状态的转换： 去重
    @Test
    public void distinctTest() {
        Stream<Integer> s1 = Stream.of(1, 2, 1, 2);
        // 把流中的重复元素去除了
        // 要去重必须的记住以前都去过的所有元素，所以这个是有状态的
        s1.distinct().forEach(System.out::println);
    }

    // 有状态的转换： 排序
    @Test
    public void sortedTest() {
        Stream<Integer> s1 = Stream.of(1, 2, 1, 2);
        s1.sorted(
                // 排序
                Comparator.comparing(i -> (Integer) i) // 这里为啥要强转一下，这里的I好像是object
                        .reversed()) // 改成降序：把结果反转
                .forEach(System.out::println);
    }


    /** =====================  将结果收集到map中 ========================= **/
    @Test
    public void toMapTest() {
        // 一个语言（中文名称）对应一个语言示例（对应的语言）
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, String> collect = locales.collect(
                Collectors.toMap(
                        l -> l.getDisplayLanguage(),
                        l -> l.getDisplayLanguage(l),
                        // 接收一个 BinaryOperator，又继承了BiFunction<T,T,T>，输入2个参数，返回一个参数
                        // 当key重复的时候，定义使用哪一个key
                        (oldVal, newVal) -> newVal
                )
        );
        System.out.println();
    }

    @Test
    public void toMapTest2() {
        // 加入我们想知道指定国家中的所有语言
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> collect = locales.collect(
                Collectors.toMap(
                        l -> l.getDisplayCountry(),
                        // 创建了一个set集合
                        l -> Collections.singleton(l.getDisplayLanguage()),
                        // 同一个国家再次出现的时候
                        // 把oldVal 和 newVal 相加，然后新的set集合
                        (oldVal, newVal) -> {
                            HashSet<String> r = new HashSet<>(oldVal);
                            r.addAll(newVal);
                            return r;
                        }
                )
        );
        System.out.println();
    }

    @Test
    public void toMapTest3() {
        // 加入我们想知道指定国家中的所有语言
        List<Apple> apps = Arrays.asList(
                new Apple("red", 120),
                new Apple("blue", 80),
                new Apple("green", 10));

        TreeMap<String, Apple> collect = apps.stream().collect(
                Collectors.toMap(
                        Apple::getColor,
                        Function.identity(), // 如果还想返回本身对象，就可以用 identity
                        (oldVal, newVal) -> {
                            // 有相同key出现的时候，抛出异常
                            throw new IllegalStateException();
                        },
                        TreeMap::new   // 默认返回的是hashmap，这里可以指定返回一种map类型
                )
        );
        System.out.println();
    }

    /** =====================  分组和分片测试 ========================= **/
    @Test
    public void groupingBy() {
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        // 按国家进行分组
        Map<String, List<Locale>> collect = locales.collect(Collectors.groupingBy(Locale::getCountry));
        System.out.println(collect);

        // 只分为两组，en的为一组，其他的为一组
        locales = Stream.of(Locale.getAvailableLocales());
        Map<Boolean, List<Locale>> en = locales.collect(Collectors.partitioningBy(l -> l.getLanguage().equals("en")));
        System.out.println(en.get(true));

        // 我要返回set而不是list
        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<Locale>> collect1 = locales.collect(Collectors.groupingBy(Locale::getCountry, Collectors.toSet()));
        System.out.println(collect1);

    }

    @Test
    public void test() {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                })
                .forEach(s -> System.out.println("forEach: " + s));

    }
}
