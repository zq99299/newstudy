package cn.mrcode.newstudy.hpbase._02.q06;

import cn.mrcode.newstudy.hpbase._01.Salary;
import cn.mrcode.newstudy.hpbase._02.q04.SalaryGroup;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <pre>
 *     用FileChannel的方式实现第四题，注意编码转换问题，并对比性能
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/10 22:09
 */
public class Practice {
    @Test
    public void main() throws IOException {
        String filePath = "resources/_02/q04/salaries";

        Instant start = Instant.now();
        System.out.println("读取并解析...");
        FileChannelReader reader = new FileChannelReader(Paths.get(filePath));
        List<Salary> salaries = new ArrayList<>(1000 * 10000);
        reader.readLine(line -> {
            Salary salary = Salary.parse(line);
            salaries.add(salary);
        });

        System.out.println("解析结束，耗时 " + Duration.between(start, Instant.now()).toMillis());
        groupAndSort(salaries);
    }

    public static void groupAndSort(List<Salary> salaries) {
        Instant start = Instant.now();
        System.out.println("分组中...");
        List<SalaryGroup> groups = new ArrayList<>();
        salaries.stream()
                .collect(Collectors.groupingBy(s -> s.getName().substring(0, 2)))
                .forEach((groupName, groupSalarys) -> {
                    Long groupYearlySalaryTotal = groupSalarys.stream().map(salary -> (long) salary.yearlySalary()).reduce(Long::sum).get();
                    SalaryGroup group = new SalaryGroup(groupName);
                    group.setYearlySalaryTotal(groupYearlySalaryTotal);
                    group.setSalarys(groupSalarys);
                    groups.add(group);
                });
        System.out.println("分组结束，耗时 " + Duration.between(start, Instant.now()).toMillis());
        start = Instant.now();
        System.out.println("排序中...");
        groups
                .stream()
//                .parallelStream() // 排序的使用不能使用并行流，否则排序数据是不正确的，这是为什么？
                .sorted(Comparator.comparing(SalaryGroup::getYearlySalaryTotal).reversed())
                .limit(10)
                .forEach(group -> {
                    System.out.println(group.getName() + " , " + group.getYearlySalaryTotal() + " , " + group.getSalarys().size());
                });
        System.out.println("排序结束，耗时 " + Duration.between(start, Instant.now()).toMillis());
    }
}
