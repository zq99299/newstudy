package cn.mrcode.newstudy.hpbase._02.q06;

import cn.mrcode.newstudy.hpbase._01.Practice_04;
import cn.mrcode.newstudy.hpbase._01.Salary;
import cn.mrcode.newstudy.hpbase._02.q02.BitsUtil;
import cn.mrcode.newstudy.hpbase._02.q04.SalaryGroup;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/15 21:35
 */
public class FileChannelPractice {
    private static String filePath = "resources/_02/q04/salariesFileChannel";
    private static int count = 1000_0000;

    @Test
    public void writeTest() {
        write();
    }

    public static void write() {
        Instant start = Instant.now();

        // 好像每次写入文件都是追加的方式写入，有啥方法 直接覆盖文件
        try (
                RandomAccessFile file = new RandomAccessFile(filePath, "rw");
                FileChannel fc = file.getChannel()
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            int rowSize = 5 + 4 + 4;
            for (int i = 0; i < count; i++) {
                if (buffer.position() + rowSize > buffer.capacity()) {
                    buffer.flip();
                    fc.write(buffer);
                    buffer.flip();
                }
                String name = Practice_04.buildName();
                buffer.put(name.getBytes());
                int baseSalary = Practice_04.build(50000, 100_0000);
                buffer.put(BitsUtil.convertBigItem(baseSalary));
                int bonus = Practice_04.build(0, 10_0000);
                buffer.put(BitsUtil.convertBigItem(bonus));
//                System.out.println(name + " , " + baseSalary + " , " + bonus);
            }
            if (buffer.hasRemaining()) {
                buffer.flip();
                fc.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("写入耗时:" + Duration.between(start, Instant.now()).toMillis());
    }

    @Test
    public void readTest() {
        read();
    }

    public static Salary[] read() {
        Instant start = Instant.now();
        Salary[] salaries = new Salary[1000 * 10000];
        try (
                RandomAccessFile file = new RandomAccessFile(filePath, "rw");
                FileChannel fc = file.getChannel()
        ) {
            int index = 0;
            byte[] nameBuf = new byte[5];
            byte[] intBuf = new byte[4];
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            while (mbb.hasRemaining()) {
                mbb.get(nameBuf);
                String name = new String(nameBuf);
                mbb.get(intBuf);
                int baseSalary = BitsUtil.getBigItem(intBuf);
                mbb.get(intBuf);
                int bonus = BitsUtil.getBigItem(intBuf);
                salaries[index++] = (Salary.newSalary(name, baseSalary, bonus));
//                System.out.println(name + " , " + baseSalary + " , " + bonus);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("读取+解析耗时:" + Duration.between(start, Instant.now()).toMillis());
        return salaries;
    }

    // 读取同时分组
    public static Map<String, SalaryGroup> readAndGroup() {
        Instant start = Instant.now();
        Map<String, SalaryGroup> groups = new HashMap<>();
        try (
                RandomAccessFile file = new RandomAccessFile(filePath, "rw");
                FileChannel fc = file.getChannel()
        ) {
            byte[] nameBuf = new byte[5];
            byte[] intBuf = new byte[4];
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            while (mbb.hasRemaining()) {
                mbb.get(nameBuf);
                String name = new String(nameBuf);
                mbb.get(intBuf);
                int baseSalary = BitsUtil.getBigItem(intBuf);
                mbb.get(intBuf);
                int bonus = BitsUtil.getBigItem(intBuf);
                Salary salary = Salary.newSalary(name, baseSalary, bonus);
                String namePrefix = salary.getName().substring(0, 2);
                if (!groups.containsKey(namePrefix)) {
                    groups.put(namePrefix, new SalaryGroup(namePrefix));
                }
                SalaryGroup salaryGroup = groups.get(namePrefix);
                salaryGroup.getSalarys().add(salary);
                salaryGroup.sum(salary.yearlySalary());
//                System.out.println(name + " , " + baseSalary + " , " + bonus);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("读取+解析+分组耗时::" + Duration.between(start, Instant.now()).toMillis());
        return groups;
    }

    // 使用拉姆达表达式，来分组和排序；
    @Test
    public void test() {
        Instant start = Instant.now();
        Salary[] salaries = read();
        Practice.groupAndSort(Arrays.asList(salaries));
        // 总耗时在11秒左右
        System.out.println("总耗时:" + Duration.between(start, Instant.now()).toMillis());
    }

    // 读取加解析加分组
    // 排序
    @Test
    public void test2() {
        Instant start = Instant.now();
        Map<String, SalaryGroup> groups = readAndGroup();
        Instant sortStart = Instant.now();
        ArrayList<SalaryGroup> salaryGroups = new ArrayList<>(groups.values());
        System.out.println("排序耗时 " + Duration.between(sortStart, Instant.now()).toMillis());
        Collections.sort(salaryGroups, Comparator.comparingLong(SalaryGroup::getYearlySalaryTotal).reversed());
        salaryGroups.stream().limit(10)
                .forEach(group -> {
                    System.out.println(group.getName() + " , " + group.getYearlySalaryTotal() + " , " + group.getSalarys().size());
                });
        System.out.println("总耗时 " + Duration.between(start, Instant.now()).toMillis());
    }
}
