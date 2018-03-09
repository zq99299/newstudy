package cn.mrcode.newstudy.hpbase._02.q04;

import cn.mrcode.newstudy.hpbase._01.Salary;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *
 *  随机生成 Salary {name, baseSalary, bonus }的记录，如“wxxx,10,1”，
 *  每行一条记录，总共1000万记录，写入文本文件（UFT-8编码），
 *  然后读取文件，name的前两个字符相同的，其年薪累加，比如wx，100万，3个人，
 *  最后做排序和分组，输出年薪总额最高的10组：
 *  wx, 200万，10人
 *  lt, 180万，8人
 *
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/7 22:26
 */
public class Practice {
    private String filePath = "resources/_02/q04/salaries";

    @Test
    public void main() throws IOException {
//        Salary[] salarys = Practice_04.mockData(1000 * 10000);
//        writeFile(salarys);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        String line = null;
        Map<String, SalaryGroup> groups = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            String[] salaryPart = line.split("\t");
            String name = salaryPart[0];
            int baseSalary = Integer.valueOf(salaryPart[1]);
            int bonus = Integer.valueOf(salaryPart[2]);

            Salary salary = new Salary();
            salary.setName(name);
            salary.setBaseSalary(baseSalary);
            salary.setBonus(bonus);
            String namePrefix = name.substring(0, 2);
            if (!groups.containsKey(namePrefix)) {
                groups.put(namePrefix, new SalaryGroup(namePrefix));
            }
            SalaryGroup salaryGroup = groups.get(namePrefix);
            salaryGroup.getSalarys().add(salary);
            salaryGroup.sum(salary.yearlySalary());
        }

        ArrayList<SalaryGroup> groupList = new ArrayList<>(groups.values());
        Collections.sort(groupList, (g1, g2) -> {
            Long yearlySalaryTotal1 = g1.getYearlySalaryTotal();
            Long yearlySalaryTotal2 = g2.getYearlySalaryTotal();
            if (yearlySalaryTotal1 > yearlySalaryTotal2) return -1;
//
            if (yearlySalaryTotal1 == yearlySalaryTotal2) return 0;
            return 1;
        });

        for (int i = 0; i < 10; i++) {
            SalaryGroup group = groupList.get(i);
            System.out.println(group.getName() + " , " + group.getYearlySalaryTotal() + " , " + group.getSalarys().size());
        }
    }

    private void writeFile(Salary[] salarys) {
        try (
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
        ) {
            for (Salary salary : salarys) {
                bufferedWriter.write(salary.getName() + "\t"
                        + salary.getBaseSalary() + "\t"
                        + salary.getBonus()
                );
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
