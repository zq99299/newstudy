package cn.mrcode.newstudy.hpbase._04.q04;

import cn.mrcode.newstudy.hpbase._01.Salary;
import cn.mrcode.newstudy.hpbase._02.q04.SalaryGroup;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用自定义的Collect实现第三题的功能
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/24 16:21
 */
public class Practice {
    private String filePath = "resources/_02/q04/salaries";

    @Test
    public void test() {
        Instant start = Instant.now();
        try (
                BufferedReader reader = Files.newBufferedReader(Paths.get(filePath)))

        {
            Stream<String> lines = reader.lines();
            Map<String, SalaryGroup> groupMap = lines
                    .map(line -> Salary.parse(line))
                    .filter(salary -> salary.yearlySalary() > 10_0000)
                    .collect(Collectors.groupingBy(salary -> salary.getName().substring(0, 2), new CollectorMy()));
            System.out.println("读+解析+分组耗时:" + Duration.between(start, Instant.now()).toMillis());
            Instant sortStart = Instant.now();
            List<SalaryGroup> result = new ArrayList<>(groupMap.values());
            result.stream()
                    .sorted(Comparator.comparing(SalaryGroup::getYearlySalaryTotal).reversed())
                    .limit(10)
                    .forEach(group -> System.out.println(group.getName() + " , " + group.getYearlySalaryTotal() + " , " + group.getSalarys().size()));
            System.out.println("排序耗时:" + Duration.between(sortStart, Instant.now()).toMillis());
            // 总耗时 9秒左右
            System.out.println("总耗时:" + Duration.between(start, Instant.now()).toMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * T, A, R :
     * T : 收集的元素，这里也就是解析成功的salary
     * A : 累积类型；作为结果集容器
     * R ：最终的结果转换
     */
    class CollectorMy implements Collector<Salary, SalaryGroup, SalaryGroup> {
        private int count = 0;

        // 初始化
        @Override
        public Supplier<SalaryGroup> supplier() {
            return () -> {
                // java.util.stream.Collectors.groupingBy(java.util.function.Function<? super T,? extends K>, java.util.function.Supplier<M>, java.util.stream.Collector<? super T,A,D>)
                // 该代码在上面的 m.computeIfAbsent(key, k -> downstreamSupplier.get()); 源码中被调用，只要key不存在，则被创建一个新对象
//                System.out.println(count++);
                return new SalaryGroup();
            };
            // 上面的代码用直接用方法引用也可以达到效果，就是看不到这里被调用了多少次了
//            return SalaryGroup::new;
        }

        // 累加器
        @Override
        public BiConsumer<SalaryGroup, Salary> accumulator() {
            // 累加器对象 是初始化产生的，根据 使用的上游架子  来确定
            // 在这里是分组，只要是同一个组的，该方法都会被调用
            // 然后就可以 计算总年薪或则其他的操作了
            return (group, salary) -> {
                String name = group.getName();
                if (name == null) {
                    group.setName(salary.getName().substring(0, 2));
                }
                group.sum(salary.yearlySalary());
                group.getSalarys().add(salary);
            };
        }

        // 并行流中需要用到的
        @Override
        public BinaryOperator<SalaryGroup> combiner() {
            return null;
        }

        // 对结果进行处理
        // 当 Collectors.groupingBy 的时候，characteristics中不包含 IDENTITY_FINISH 该方法被执行
        @Override
        public Function<SalaryGroup, SalaryGroup> finisher() {
            return salaryGroup -> salaryGroup;
        }

        @Override
        public Set<Characteristics> characteristics() {

            return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
//            return new HashSet<Characteristics>();
        }
    }
}
