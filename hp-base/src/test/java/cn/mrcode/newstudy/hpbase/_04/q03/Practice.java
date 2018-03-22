package cn.mrcode.newstudy.hpbase._04.q03;

import cn.mrcode.newstudy.hpbase._01.Salary;
import cn.mrcode.newstudy.hpbase._02.q04.SalaryGroup;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 *     用Stream的API实现第四题的结果，其中增加一个过滤条件，即年薪大于10万的才被累加，
 *     分别用ParellStream与普通Stream来运算，看看效果的差距
 *
 *     这个题目是第02课程中第4题的作业补充。
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/21 22:12
 */
public class Practice {
    private String filePath = "resources/_02/q04/salaries";

    // 读 + 解析 + 分组
    @Test
    public void stream() {
        Instant start = Instant.now();
        List<SalaryGroup> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            Stream<String> lines = reader.lines();
            System.out.println(lines.isParallel());  // 返回的不是并行流
            lines
                    .map(line -> Salary.parse(line))
                    .filter(salary -> salary.yearlySalary() > 10_0000)
                    .collect(Collectors.groupingBy(salary -> salary.getName().substring(0, 2)))
                    .forEach((namePrefix, list) -> {
                        SalaryGroup e = new SalaryGroup(namePrefix);
                        e.setSalarys(list);
                        list.stream().forEach(i -> e.sum(i.yearlySalary()));
                        result.add(e);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("读+解析+分组耗时:" + Duration.between(start, Instant.now()).toMillis());

        Instant sortStart = Instant.now();
        result.stream()
                .sorted(Comparator.comparing(SalaryGroup::getYearlySalaryTotal).reversed())
                .limit(10)
                .forEach(group -> System.out.println(group.getName() + " , " + group.getYearlySalaryTotal() + " , " + group.getSalarys().size()));
        System.out.println("排序耗时:" + Duration.between(sortStart, Instant.now()).toMillis());
        // 总耗时 9秒左右
        System.out.println("总耗时:" + Duration.between(start, Instant.now()).toMillis());
    }

    // 读+解析  ： 分组 再排序
    @Test
    public void stream2() {
        Instant start = Instant.now();
        List<Salary> salaries = loadData(start);

        Instant groupStart = Instant.now();
        List<SalaryGroup> result = new ArrayList<>();
        salaries
//                .stream()
                .parallelStream()
//                .filter(salary -> salary.yearlySalary() > 10_0000)
//                .collect(Collectors.groupingBy(salary -> salary.getName().substring(0, 2)))
                .collect(Collectors.groupingByConcurrent(salary -> salary.getName().substring(0, 2)))
                .forEach((namePrefix, list) -> {
                    SalaryGroup e = new SalaryGroup(namePrefix);
                    e.setSalarys(list);
                    list.stream().forEach(i -> e.sum(i.yearlySalary()));
                    result.add(e);
                });
        System.out.println("分组耗时:" + Duration.between(groupStart, Instant.now()).toMillis());
        Instant sortStart = Instant.now();
        result.stream()
                .sorted(Comparator.comparing(SalaryGroup::getYearlySalaryTotal).reversed())
                .limit(10)
                .forEach(group -> System.out.println(group.getName() + " , " + group.getYearlySalaryTotal() + " , " + group.getSalarys().size()));
        System.out.println("排序耗时:" + Duration.between(sortStart, Instant.now()).toMillis());
        System.out.println("总耗时:" + Duration.between(start, Instant.now()).toMillis());
    }

    private List<Salary> loadData(Instant start) {

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {

            List<Salary> salarys = reader.lines()
                    .map(line -> Salary.parse(line))
                    .collect(Collectors.toList());
            System.out.println("读+解析耗时:" + Duration.between(start, Instant.now()).toMillis());
            return salarys;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
