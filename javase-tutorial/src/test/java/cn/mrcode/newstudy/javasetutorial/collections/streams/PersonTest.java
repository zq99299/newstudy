package cn.mrcode.newstudy.javasetutorial.collections.streams;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ${desc}
 * @author zhuqiang
 * @version 1.0.1 2017/12/8 11:10
 * @date 2017/12/8 11:10
 * @since 1.0
 */
public class PersonTest {
    List<Person> roster = new ArrayList<>();

    @Before
    public void mockdata() {
        roster.add(new Person("x1", LocalDate.now(), Person.Sex.FEMALE, "x1@qq.com", 1));
        roster.add(new Person("x2", LocalDate.now(), Person.Sex.FEMALE, "x2@qq.com", 2));
        roster.add(new Person("x3", LocalDate.now(), Person.Sex.FEMALE, "x3@qq.com", 3));
        roster.add(new Person("x4", LocalDate.now(), Person.Sex.MALE, "x4@qq.com", 4));
        roster.add(new Person("x5", LocalDate.now(), Person.Sex.MALE, "x5@qq.com", 5));
        roster.add(new Person("x6", LocalDate.now(), Person.Sex.MALE, "x6@qq.com", 6));
    }

    @Test
    public void foreach() {
        for (Person p : roster) {
            System.out.println(p.getName());
        }
    }

    @Test
    public void streamForeach() {
        roster.stream().forEach(p -> System.out.println(p.getName()));
    }

    // 一个管道是一系列聚合操作序列。以下示例使用由聚合操作filter和forEach组成的管道将集合名单中包含的男性成员打印出来
    @Test
    public void fun3() {
        roster
                .stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .forEach(p -> System.out.println(p.getName()));

        for (Person p : roster) {
            if (p.getGender() == Person.Sex.MALE) {
                System.out.println(p.getName());
            }
        }
    }

    @Test
    public void fun4() {
        double average = roster
                .stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .mapToInt(Person::getAge)
                .average()
                .getAsDouble();

        System.out.println(average);

        int sum = roster
                .stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .mapToInt(Person::getAge)
                .sum();
        System.out.println(sum);

        Integer totalAge = roster
                .stream()
                .map(Person::getAge)
                .reduce(
                        0,
                        (a, b) -> a + b);
        System.out.println(totalAge);

        Integer reduce = roster
                .stream()
                .map(Person::getAge)
                .reduce(
                        0,
                        (a, b) -> {
                            return a + b;
                        });
        System.out.println(reduce);
    }

    @Test
    public void fun5() {
        Averager averageCollect = roster.stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .map(Person::getAge)
                .collect(Averager::new, Averager::accept, (averager, averager2) -> {
                });

        System.out.println("Average age of male members: " +
                                   averageCollect.average());
    }

    @Test
    public void fun6() {
        List<String> namesOfMaleMembersCollect = roster
                .stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .map(p -> p.getName())
                .collect(Collectors.toList());
        System.out.println(namesOfMaleMembersCollect);
    }

    @Test
    public void fun7() {
        Map<Person.Sex, List<Person>> byGender =
                roster
                        .stream()
                        .collect(
                                Collectors.groupingBy(Person::getGender));

        System.out.println(byGender);
    }

    // 以下示例检索roster集合中每个成员的名称，并按性别进行分组：
    @Test
    public void fun8() {
        Map<Person.Sex, List<String>> namesByGender =
                roster
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        Person::getGender,
                                        Collectors.mapping(
                                                Person::getName,
                                                Collectors.toList())));

        System.out.println(namesByGender);
    }

    // 以下示例检索每个性别成员的总年龄：
    @Test
    public void fun9() {
        Map<Person.Sex, Integer> totalAgeByGender =
                roster
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        Person::getGender,
                                        Collectors.reducing(
                                                0,
                                                Person::getAge,
                                                Integer::sum)));

        System.out.println(totalAgeByGender);
    }

    // 以下示例检索每个性别成员的平均年龄：
    @Test
    public void fun10() {
        Map<Person.Sex, Double> averageAgeByGender = roster
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Person::getGender,
                                Collectors.averagingInt(Person::getAge)
                        ));

        System.out.println(averageAgeByGender);
    }
}