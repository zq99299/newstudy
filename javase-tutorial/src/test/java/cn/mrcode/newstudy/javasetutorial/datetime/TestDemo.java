package cn.mrcode.newstudy.javasetutorial.datetime;

import org.junit.Test;

import java.time.*;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.time.temporal.*;
import java.util.*;

/**
 * 累计了比较多的测试代码
 * @author zhuqiang
 * @version 1.0.0 2018/5/7 14:36
 * @date 2018/5/7 14:36
 * @since 1.0.0
 */
public class TestDemo {
    @Test
    public void fun1() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println(now.with(ChronoField.YEAR, 2017));
        // 加1天
        System.out.println(now.plus(1, ChronoUnit.DAYS));
        //2018年5月7日 下午03时34分23秒
        System.out.println(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).format(now));
        // 2018-05-07 15:34:47
        System.out.println(format(now));

    }

    public String format(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(localDateTime);
    }


    @Test
    public void fun2() {
        DayOfWeek dow = DayOfWeek.MONDAY;
        Locale locale = Locale.getDefault();
        // 星期一
        System.out.println(dow.getDisplayName(TextStyle.FULL, locale));
        // 一
        System.out.println(dow.getDisplayName(TextStyle.NARROW, locale));
        // 星期一
        System.out.println(dow.getDisplayName(TextStyle.SHORT, locale));
    }

    @Test
    public void fun3() {
        System.out.printf("%d%n", Month.FEBRUARY.maxLength());

        Month month = Month.AUGUST;
        Locale locale = Locale.getDefault();
        System.out.println(month);
        System.out.println(month.getDisplayName(TextStyle.FULL, locale));
        System.out.println(month.getDisplayName(TextStyle.NARROW, locale));
        System.out.println(month.getDisplayName(TextStyle.SHORT, locale));
    }

    @Test
    public void fun4() {
        // 2000年11月20日 星期一
        LocalDate date = LocalDate.of(2000, Month.NOVEMBER, 20);
        // 当前指定日期的下一个 星期三
        LocalDate nextWed = date.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        // 当前指定日期的下一个 星期一
        LocalDate nextMond = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        System.out.println(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date));
        System.out.println(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(nextWed));
        System.out.println(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(nextMond));
    }

    @Test
    public void fun5() {
        DayOfWeek dotw = LocalDate.of(2012, Month.JULY, 9).getDayOfWeek();
        System.out.println(dotw);
    }

    @Test
    public void fun6() {
        LocalDate date = LocalDate.of(2000, Month.NOVEMBER, 20);
        // 下一个周三是几号？
        TemporalAdjuster adj = TemporalAdjusters.next(DayOfWeek.WEDNESDAY);
        LocalDate nextWed = date.with(adj);  // with等同于setter方法，只不过大部分日期类都是固定的
        System.out.printf("For the date of %s, the next Wednesday is %s.%n",
                          date, nextWed);
    }

    @Test
    public void fun7() {
        // 2018-05: 31
        YearMonth date = YearMonth.now();
        System.out.printf("%s: %d%n", date, date.lengthOfMonth());

        //2010-02: 28
        YearMonth date2 = YearMonth.of(2010, Month.FEBRUARY);
        System.out.printf("%s: %d%n", date2, date2.lengthOfMonth());

        // 2012-02: 29
        YearMonth date3 = YearMonth.of(2012, Month.FEBRUARY);
        System.out.printf("%s: %d%n", date3, date3.lengthOfMonth());

        // 2012-02: 366  // 返回该年有多少天
        System.out.printf("%s: %d%n", date3, date3.lengthOfYear());
    }

    @Test
    public void fun8() {
        // 2月29号
        MonthDay date = MonthDay.of(Month.FEBRUARY, 29);
        // 对于 2010 年是否是有效的时间
        boolean validLeapYear = date.isValidYear(2010);
        System.out.println(validLeapYear);
    }

    @Test
    public void fun9() {
        LocalTime thisSec;

        for (; ; ) {
            thisSec = LocalTime.now();

            // implementation of display code is left to the reader
            display(thisSec.getHour(), thisSec.getMinute(), thisSec.getSecond());
        }
    }

    private void display(int hour, int minute, int second) {
        System.out.println(hour + ":" + minute + ":" + second);
    }

    @Test
    public void fun19() {
        System.out.printf("now: %s%n", LocalDateTime.now());

        // 1994年4月15号11点30分
        System.out.printf("Apr 15, 1994 @ 11:30am: %s%n",
                          LocalDateTime.of(1994, Month.APRIL, 15, 11, 30));

        // 基于 瞬时类 只有纳秒 + 时区id
        System.out.printf("now (from Instant): %s%n",
                          LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

        // 6月后
        System.out.printf("6 months from now: %s%n",
                          LocalDateTime.now().plusMonths(6));

        // 6月前
        System.out.printf("6 months ago: %s%n",
                          LocalDateTime.now().minusMonths(6));
    }

    /**
     * 获取所有的可用时区，并打印出非整点偏移量
     */
    @Test
    public void fun20() {
        // 获取所有可用的时区
        Set<String> allZones = ZoneId.getAvailableZoneIds();

        // 按自然顺序排序
        // Create a List using the set of zones and sort it.
        List<String> zoneList = new ArrayList<String>(allZones);
        Collections.sort(zoneList);

        LocalDateTime dt = LocalDateTime.now();
        for (String s : zoneList) {
            // 获取到的字符串可以通过ZoneId.of获取实例
            ZoneId zone = ZoneId.of(s);
            // 把本地时间加时区信息 转换成一个ZonedDateTime
            // 但是这个LocalDateTime不包含时区信息，是怎么计算出来的呢？本地时间与这个时区相差n小时？
            // 这里的偏移量是针对 格林威治标准时间来说的 +3 ，就是比标准时间快3个小时
            // 如果说一个时区是 +3;而北京是+8，那么该时区比北京慢5个小时
            // 北京时间是12点，那么该时区12-5 = 7
            ZonedDateTime zdt = dt.atZone(zone);
            ZoneOffset offset = zdt.getOffset();
            int secondsOfHour = offset.getTotalSeconds() % (60 * 60);
            String out = String.format("%35s %10s%n", zone, offset);

            // Write only time zones that do not have a whole hour offset
            // to standard out.
            if (secondsOfHour != 0) {
                System.out.printf(out);
            }
        }
    }

    /***
     * 从美国洛杉矶出发到东京：换算出美国洛杉矶飞机出发的时间  和  到达东京的时间；
     */
    @Test
    public void fun21() {
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy  hh:mm a");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY-MM-dd  HH:mm:ss");

        // Leaving from San Francisco on July 20, 2013, at 7:30 p.m.
        //  2013-07-20  19:30:00
        LocalDateTime leaving = LocalDateTime.of(2013, Month.JULY, 20, 19, 30);
        ZoneId leavingZone = ZoneId.of("America/Los_Angeles");
        ZonedDateTime departure = ZonedDateTime.of(leaving, leavingZone);

        try {
            String out1 = departure.format(format);
            System.out.printf("LEAVING:  %s (%s)%n", out1, leavingZone);
        } catch (DateTimeException exc) {
            System.out.printf("%s can't be formatted!%n", departure);
            throw exc;
        }

        // Flight is 10 hours and 50 minutes, or 650 minutes
        ZoneId arrivingZone = ZoneId.of("Asia/Tokyo");
        // 使用美国洛杉矶出发的时间，然后换算成东京的时区，返回该时区对应的时间
        ZonedDateTime arrival = departure.withZoneSameInstant(arrivingZone)
                .plusMinutes(650); // 在该时区的基础上加650分钟

        try {
            String out2 = arrival.format(format);
            System.out.printf("ARRIVING: %s (%s)%n", out2, arrivingZone);
        } catch (DateTimeException exc) {
            System.out.printf("%s can't be formatted!%n", arrival);
            throw exc;
        }

        // 夏令时
        if (arrivingZone.getRules().isDaylightSavings(arrival.toInstant()))
            System.out.printf("  (%s daylight saving time will be in effect.)%n",
                              arrivingZone);
        else
            // 标准时间
            System.out.printf("  (%s standard time will be in effect.)%n",
                              arrivingZone);
    }

    @Test
    public void fun22() {
        // 2017.07.20 19:30
        LocalDateTime localDate = LocalDateTime.of(2013, Month.JULY, 20, 19, 30);
        ZoneOffset offset = ZoneOffset.of("-08:00");

        OffsetDateTime offsetDate = OffsetDateTime.of(localDate, offset);

        // 当前时间月中的最后一个周4
        OffsetDateTime lastThursday =
                offsetDate.with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));
        System.out.printf("The last Thursday in July 2013 is the %sth.%n",
                          lastThursday.getDayOfMonth());

        // 但是并没有看出来有什么作用
    }

    @Test
    public void fun23() {
        long secondsFromEpoch = Instant.ofEpochSecond(0L).until(Instant.now(),
                                                                ChronoUnit.SECONDS);

        LocalDateTime start = LocalDateTime.of(2018, 05, 01, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2018, 05, 8, 0, 0, 0);
        // 两个时间之间相差了7天
        start.until(end, ChronoUnit.DAYS); // 还有其他时间类都提供了 unitl方法
    }

    @Test
    public void fun24() {
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        System.out.printf("%s %d %d at %d:%d%n", ldt.getMonth(), ldt.getDayOfMonth(),
                          ldt.getYear(), ldt.getHour(), ldt.getMinute());
    }

    // 偏移量和时区id的用法
    @Test
    public void fun25() {
        // 一个不带任何时区的时间
        LocalDateTime date = LocalDateTime.of(2018, 05, 01, 0, 0, 0);

        ZonedDateTime d1 = ZonedDateTime.of(date, ZoneId.systemDefault());

        ZoneOffset offset = ZoneOffset.of("+08:00");
        OffsetDateTime d2 = OffsetDateTime.of(date, offset);

        // 2018-05-01T00:00+08:00[GMT+08:00]
        // ZoneId 带了具体的ID
        System.out.println(d1);
        // 2018-05-01T00:00+08:00
        // 而偏移没有ID,因为多个ID对应的值有可能是一样的
        System.out.println(d2);

        // 那么把中国时间变成其他的时间
        // 2018-04-30T20:00+04:00[Asia/Yerevan]
        // 把该时间转换成指定时区了
        d1.withZoneSameInstant(ZoneId.of("Asia/Yerevan"));
        // 2018-05-01T00:00+04:00[Asia/Yerevan]
        // 只是改变了时区
        d1.withZoneSameLocal(ZoneId.of("Asia/Yerevan"));

        // 上两个api的时间，
    }

    @Test
    public void fun26() {
        // 2000 10 15
        LocalDate date = LocalDate.of(2000, Month.OCTOBER, 15);
        DayOfWeek dotw = date.getDayOfWeek(); // 获取当天是周几
        System.out.printf("%s is on a %s%n", date, dotw);

        System.out.printf("first day of Month: %s%n",
                          date.with(TemporalAdjusters.firstDayOfMonth())); // 当月第一天
        System.out.printf("first Monday of Month: %s%n",
                          date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))); // 当月第一个周一
        System.out.printf("last day of Month: %s%n",
                          date.with(TemporalAdjusters.lastDayOfMonth())); // 当月最后一天
        System.out.printf("first day of next Month: %s%n",
                          date.with(TemporalAdjusters.firstDayOfNextMonth())); // 基于当前月的下个月第一天
        System.out.printf("first day of next Year: %s%n",
                          date.with(TemporalAdjusters.firstDayOfNextYear())); // 基于当前时间 下一年的第一天
        System.out.printf("first day of Year: %s%n",
                          date.with(TemporalAdjusters.firstDayOfYear())); // 基于当年的第一天
    }

    @Test
    public void fun27() {
        LocalDate d1 = LocalDate.of(2018, 05, 13);
        LocalDate d2 = LocalDate.of(2018, 05, 16);
        PaydayAdjuster adjuster = new PaydayAdjuster();
        System.out.println(d1.with(adjuster));
        System.out.println(d2.with(adjuster));
    }

    public class PaydayAdjuster implements TemporalAdjuster {

        /**
         * The adjustInto method accepts a Temporal instance
         * and returns an adjusted LocalDate. If the passed in
         * parameter is not a LocalDate, then a DateTimeException is thrown.
         */
        public Temporal adjustInto(Temporal input) {
            LocalDate date = LocalDate.from(input);
            int day;
            if (date.getDayOfMonth() < 15) {
                day = 15;
            } else {
                // 如果大于15号，则当月最后一天
                day = date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
            }
            date = date.withDayOfMonth(day);
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                // 如果是周6或周日，则当前时间的前一个星期五
                // 也就是：如果遇到发工资那天是星期周六日的话，则提前到周五
                date = date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
            }

            return input.with(date);
        }
    }

    @Test
    public void fun28() {
        // 精度查询，不过返回的是英文
        TemporalQuery<TemporalUnit> query = TemporalQueries.precision();
        System.out.printf("LocalDate precision is %s%n",
                          LocalDate.now().query(query));
        System.out.printf("LocalDateTime precision is %s%n",
                          LocalDateTime.now().query(query));
        System.out.printf("Year precision is %s%n",
                          Year.now().query(query));
        System.out.printf("YearMonth precision is %s%n",
                          YearMonth.now().query(query));
        System.out.printf("Instant precision is %s%n",
                          Instant.now().query(query));
    }

    /**
     * 自定义查询
     */
    @Test
    public void fun29() {
        LocalDate date = LocalDate.now();
        // 不使用拉姆达表达式查询
        Boolean isFamilyVacation = date.query(new FamilyVacations());

        // 使用拉姆达表达式查询
        Boolean isFamilyBirthday = date.query(FamilyBirthdays::isFamilyBirthday);

        if (isFamilyVacation.booleanValue() || isFamilyBirthday.booleanValue())
            System.out.printf("%s 是一个重要的日子!%n", date);
        else
            System.out.printf("%s 不是一个重要的日子.%n", date);
    }

    public class FamilyVacations implements TemporalQuery<Boolean> {
        @Override
        public Boolean queryFrom(TemporalAccessor date) {
            int month = date.get(ChronoField.MONTH_OF_YEAR);
            int day = date.get(ChronoField.DAY_OF_MONTH);

            // Disneyland over Spring Break
            // 4月 3号 ~ 4月8号 （包括）
            if ((month == Month.APRIL.getValue()) && ((day >= 3) && (day <= 8)))
                return Boolean.TRUE;

            // Smith family reunion on Lake Saugatuck
            // 8月 8号~14号 （包括）
            if ((month == Month.AUGUST.getValue()) && ((day >= 8) && (day <= 14)))
                return Boolean.TRUE;

            return Boolean.FALSE;
        }
    }

    public static class FamilyBirthdays {
        // 只检查月和日
        public static Boolean isFamilyBirthday(TemporalAccessor date) {
            int month = date.get(ChronoField.MONTH_OF_YEAR);
            int day = date.get(ChronoField.DAY_OF_MONTH);

            // Angie's 的生日是4月3号
            if ((month == Month.APRIL.getValue()) && (day == 3))
                return Boolean.TRUE;

            // Sue's 的生日是6月18号
            if ((month == Month.JUNE.getValue()) && (day == 18))
                return Boolean.TRUE;

            // Joe's 的生日是5月29号
            if ((month == Month.MAY.getValue()) && (day == 29))
                return Boolean.TRUE;

            return Boolean.FALSE;
        }
    }

    @Test
    public void fun30() {
        Instant start = Instant.now();
        Duration gap = Duration.ofSeconds(10);
        Instant later = start.plus(gap);
        System.out.println(later);
    }

    @Test
    public void fun31() {
        Instant current = Instant.now();
        // 10秒前
        Instant previous = current.minus(10, ChronoUnit.SECONDS);
        if (previous != null) {
            // 计算两个时间之前间隔多少毫秒
            long between = ChronoUnit.MILLIS.between(previous, current);
            System.out.println(between); // 10000
        }
    }

    @Test
    public void fun32() {
        LocalDate today = LocalDate.now();
        // 1960.06.01
        LocalDate birthday = LocalDate.of(1960, Month.JANUARY, 1);

        Period p = Period.between(birthday, today);
        long p2 = ChronoUnit.DAYS.between(birthday, today);
        // 生活了58年，4个月，8天，总共21313天
        System.out.println("You are " + p.getYears() + " years, " + p.getMonths() +
                                   " months, and " + p.getDays() +
                                   " days old. (" + p2 + " days total)");

    }

    @Test
    public void fun33() {
        LocalDateTime date = LocalDateTime.of(2013, Month.JULY, 20, 19, 30);
        JapaneseDate jdate = JapaneseDate.from(date);
        HijrahDate hdate = HijrahDate.from(date);
        // 中华民国 台湾
        MinguoDate mdate = MinguoDate.from(date);
        ThaiBuddhistDate tdate = ThaiBuddhistDate.from(date);
    }

    @Test
    public void fun34() {
        Calendar now = Calendar.getInstance();
        ZonedDateTime zdt = ZonedDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
    }

    @Test
    public void fun35() {
        GregorianCalendar cal = new GregorianCalendar();

        TimeZone tz = cal.getTimeZone();
        int tzoffset = cal.get(Calendar.ZONE_OFFSET); // 获取偏移量

        ZonedDateTime zdt = cal.toZonedDateTime();

        GregorianCalendar newCal = GregorianCalendar.from(zdt);

        LocalDateTime ldt = zdt.toLocalDateTime();
        LocalDate date = zdt.toLocalDate();
        LocalTime time = zdt.toLocalTime();
    }
}
