package cn.mrcode.newstudy.javasetutorial.datetime;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
/*
 * Convert LocalDate -> ChronoLocalDate -> String and back.
 */

public class StringConverter {

    /**
     * 将LocalDate（ISO）值转换为日期日期日期
     * 使用所提供的年表，然后格式化
     * 使用DateTimeFormatter与一个字符串的日期时间
     * 基于年表和当前地区的短模式。
     * @param localDate - ISO日期转换和格式。
     * @param chrono    - 可选的日历年表，如果为空则默认使用IsoChronology
     */
    public static String toString(LocalDate localDate, Chronology chrono) {
        if (localDate != null) {
            // 特定功能获取/设置缺省语言环境。
            // 获取默认的语言环境
            Locale locale = Locale.getDefault(Locale.Category.FORMAT);
            ChronoLocalDate cDate;
            if (chrono == null) {
                chrono = IsoChronology.INSTANCE;
            }
            try {
                cDate = chrono.date(localDate);
            } catch (DateTimeException ex) {
                System.err.println(ex);
                chrono = IsoChronology.INSTANCE;
                cDate = localDate;
            }
            String pattern = "M/d/yyyy GGGGG";
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(pattern);
            return dateFormatter.format(cDate);
        } else {
            return "";
        }
    }

    /**
     * 使用DateTimeFormatter将字符串解析为计时日期
     * 基于当前语言环境的短模式
     * 提供年表，然后将其转换为LocalDate（ISO）值。
     * @param text   - 已简短的格式输入日期文本
     * @param chrono - 可选的日历年表，如果为空则默认使用IsoChronology
     */
    public static LocalDate fromString(String text, Chronology chrono) {
        if (text != null && !text.isEmpty()) {
            Locale locale = Locale.getDefault(Locale.Category.FORMAT);
            if (chrono == null) {
                chrono = IsoChronology.INSTANCE;
            }
            String pattern = "M/d/yyyy GGGGG";
            DateTimeFormatter df = new DateTimeFormatterBuilder().parseLenient()
                    .appendPattern(pattern)
                    .toFormatter()
                    .withChronology(chrono)
                    .withDecimalStyle(DecimalStyle.of(locale));
            TemporalAccessor temporal = df.parse(text);
            ChronoLocalDate cDate = chrono.date(temporal);
            return LocalDate.from(cDate);
        }
        return null;
    }

    public static void main(String[] args) {
        LocalDate date = LocalDate.of(1996, Month.OCTOBER, 29);
        System.out.printf("%s%n",
                          StringConverter.toString(date, JapaneseChronology.INSTANCE));
        System.out.printf("%s%n",
                          StringConverter.toString(date, MinguoChronology.INSTANCE));
        System.out.printf("%s%n",
                          StringConverter.toString(date, ThaiBuddhistChronology.INSTANCE));
        System.out.printf("%s%n",
                          StringConverter.toString(date, HijrahChronology.INSTANCE));


        System.out.printf("%s%n", StringConverter.fromString("10/29/0008 H",
                                                             JapaneseChronology.INSTANCE));
        System.out.printf("%s%n",
                          StringConverter.fromString("10/29/0085 1",
                                                     MinguoChronology.INSTANCE));
        System.out.printf("%s%n",
                          StringConverter.fromString("10/29/2539 B.E.",
                                                     ThaiBuddhistChronology.INSTANCE));
        System.out.printf("%s%n",
                          StringConverter.fromString("6/16/1417 1",
                                                     HijrahChronology.INSTANCE));
    }
}
