package cn.mrcode.newstudy.hpbase._01;

import java.util.Arrays;
import java.util.Random;

/**
 * <pre>
 * 第4题目：
 *   定义Java类Salary {String name, int baseSalary, int bonus },随机产生1万个实例，
 *   属性也随机产生（baseSalary范围是5-100万，bonus为（0-10万），其中name长度为5，随机字符串，
 *   然后进行排序，排序方式为收入总和（baseSalary*13+bonus），输出收入最高的10个人的名单
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/1 21:54
 */
public class Practice_04 {
    private static Random random = new Random();

    public static void main(String[] args) {
        Salary[] salaries = mockData();
        Arrays.sort(salaries, (s1, s2) -> {
            int s1Total = s1.getBaseSalary() * 13 + s1.getBonus();
            int s2Total = s2.getBaseSalary() * 13 + s2.getBonus();

            if (s1Total < s2Total) return 1;
            if (s1Total == s2Total) return 0;
            return -1;
        });
        Salary[] top10 = Arrays.copyOf(salaries, 10);
        for (Salary salary : top10) {
            System.out.println(salary.getName() + " : " + (salary.getBaseSalary() * 13 + salary.getBonus()));
        }
    }

    public static Salary[] mockData() {
       return mockData(10000);
    }

    public static Salary[] mockData(int total) {
        Salary[] salaries = new Salary[total];
        for (int i = 0; i < salaries.length; i++) {
            Salary salary = new Salary();
            salary.setName(buildName());
            salary.setBaseSalary(build(50000, 100_0000));
            salary.setBonus(build(0, 10_0000));
            salaries[i] = salary;
        }
        return salaries;
    }

    private static int build(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }


    private static String buildName() {
        // ASCII 码，A = 65;Z=90
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 5; i++) {
            int c = random.nextInt(90 - 65 + 1) + 65;
            sb.append((char) c);
        }
        return sb.toString();
    }
}
