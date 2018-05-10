package cn.mrcode.newstudy.javasetutorial.datetime;

import org.junit.Test;

import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/05/09     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/5/9 14:23
 * @date 2018/5/9 14:23
 * @since 1.0.0
 */
public class QandE {
    // 给定一个随机日期，你如何找到上个星期四的日期？
    @Test
    public void fun1() {
        LocalDate date = LocalDate.now();
        // 通过TemporalAdjusters.previous返回一个函数
        // 对于该题来说，源码中 也是获取星期几然后进行往前推进几天，源码很简单
        LocalDate with = date.with(TemporalAdjusters.previous(DayOfWeek.THURSDAY));
    }

    // 你如何将Instant转换为ZonedDateTime？你会如何转换一个ZonedDateTime到Instant？
    @Test
    public void fun2() {
        Instant now = Instant.now();
        // instant 不包含时区信息，所以需要提供一个时区
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, ZoneId.systemDefault());
        zonedDateTime.toInstant();
    }

    // 给定一个年份，打印出该年中每个月有多少天
    @Test
    public void fun3() {
        int year = 2018;

        Stream.of(Month.values())
                // 注意这里：可以使用 YearMonth.of(year, month) 类
                .map(month -> LocalDate.of(year, month.getValue(), 1))
                .forEach(date -> {
                    Month month = date.getMonth();
                    String displayNameMonth = month.getDisplayName(TextStyle.FULL, Locale.getDefault());
//                    System.out.printf("%s: %d days%n", displayNameMonth, date.lengthOfMonth());
                    System.out.format("%3s %3s%n", displayNameMonth, date.lengthOfMonth());
                });
    }

    // 写一个例子，在当年的某个特定月份，列出当月的所有星期一。
    @Test
    public void fun4() {
        int m = 5;
        // LocalDate date = LocalDate.of(2018, month, 1);
        Month month = Month.of(m);
        // 注意这里的at用法，挺不错
        LocalDate date = Year.now().atMonth(month).atDay(1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));  // 找到该月的第一个星期并作为起始日期
        Month mi = date.getMonth();
        System.out.printf("%s的星期一有以下几天:%n", month.getDisplayName(TextStyle.FULL, Locale.getDefault()));
        while (mi == month) {
            System.out.printf("%s%n", date);
            date = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            mi = date.getMonth();
        }
    }

    // 写一个例子，测试一个给定的日期是否发生在13日星期五。
    @Test
    public void fun5() {
        // 假设给定5月13号，判断该日期是否是13号并且还是星期五
        int m = 5;
        int day = 13;

        LocalDate date = Year.now().atMonth(m).atDay(day);
        // 使用查询方式来处理是最方便的
        Boolean query = date.query(temporal -> {
            int dom = temporal.get(ChronoField.DAY_OF_MONTH);
            int dow = temporal.get(ChronoField.DAY_OF_WEEK);
            return dom == 13 && dow == 5;
        });
        System.out.println(query);
    }

    // 那么扩展一下，找一年中是13号又是星期5的日期
    @Test
    public void fun6() {
        int year = 2018;
        Year y = Year.of(year);
        LocalDate date = y.atMonth(1).atDay(1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.FRIDAY));
        int targetY = date.getYear();
        while (year == targetY) {
            Boolean query = date.query(temporal -> {
                int dom = temporal.get(ChronoField.DAY_OF_MONTH);
                int dow = temporal.get(ChronoField.DAY_OF_WEEK);
                return dom == 13 && dow == 5;
            });
            if (query) {
                System.out.println(date);
            }
            date = date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
            targetY = date.getYear();
        }
    }
}
