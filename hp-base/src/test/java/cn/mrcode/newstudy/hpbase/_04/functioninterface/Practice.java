package cn.mrcode.newstudy.hpbase._04.functioninterface;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.*;

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
}
