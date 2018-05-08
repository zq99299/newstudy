package cn.mrcode.newstudy.javasetutorial.datetime;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.util.Locale;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/05/08     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/5/8 14:42
 * @date 2018/5/8 14:42
 * @since 1.0.0
 */
public class FormatterTest {
    @Test
    public void fun1() {
        String in = "20111203";
        LocalDate date = LocalDate.parse(in, DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(date);
    }

    @Test
    public void fun2() {
        // 注意这个程序；MMM 三个m的 是需要本地语言环境下汉化
        // 比如下面这个字符串"四月 03 2003"  否则会报错的
        String input = Month.APRIL.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " 03 2003";
        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("MMM d yyyy");
            LocalDate date = LocalDate.parse(input, formatter);
            System.out.printf("%s%n", date);
        } catch (DateTimeParseException exc) {
            System.out.printf("%s is not parsable!%n", input);
            throw exc;      // Rethrow the exception.
        }
        // 'date' has been successfully parsed
    }

    @Test
    public void fun3() {
        // 之前说道了MMM的解析包括am等需要和本地语言环境挂钩，
        // 所以这里需要时区信息
        ZoneId leavingZone = ZoneId.systemDefault();
        ZonedDateTime departure = ZonedDateTime.of(LocalDateTime.now(), leavingZone);

        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy  hh:mm a");
            String out = departure.format(format);
            // LEAVING:  五月 8 2018  03:02 下午 (GMT+08:00)
            System.out.printf("LEAVING:  %s (%s)%n", out, leavingZone);
        } catch (DateTimeException exc) {
            System.out.printf("%s can't be formatted!%n", departure);
            throw exc;
        }
    }

    @Test
    public void fun4() {
        // 是否支持 am.pm 上午下午这样的字段
        // 由于 LocalDate 不包含时分秒，所以不支持
        boolean isSupported = LocalDate.now().isSupported(ChronoField.CLOCK_HOUR_OF_DAY);
    }

    @Test
    public void fun5() {
        Instant time = Instant.now();
        // 返回当前时间的毫秒数也就是秒后面的毫秒数0-999
        int i = time.get(ChronoField.MILLI_OF_SECOND);
        LocalDateTime date = LocalDateTime.now();
        // 返回了当前日期所属日期
        int qoy = date.get(IsoFields.QUARTER_OF_YEAR);
        // 当前毫秒：256
        System.out.println("当前毫秒：" + i);
        // 所属季度：2
        System.out.println("所属季度：" + qoy);
    }

    @Test
    public void fun6() {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(dtf.format(LocalDateTime.now()));
        System.out.println(LocalDateTime.parse("2018-05-08 16:03:55", dtf));
        dtf.parse("2018-05-08 16:03:55").query(LocalDateTime::from);
    }
}
