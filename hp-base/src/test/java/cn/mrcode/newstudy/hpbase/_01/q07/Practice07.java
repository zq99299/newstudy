package cn.mrcode.newstudy.hpbase._01.q07;

import cn.mrcode.newstudy.hpbase._01.Practice_04;
import cn.mrcode.newstudy.hpbase._01.Salary;
import cn.mrcode.newstudy.hpbase._01.q05.MyItem;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 * <pre>
 * DualPivotQuicksort算法与普通冒泡算法相比，有哪些改进?
 * 对比常见的几种基于数组的排序算法，说说为什么Java选择了快排?
 *
 * 以下是加分题目
 * 第一：
 *      写出标准冒泡排序与快速排序的算法，排序对象为上面说的 Salary {name, baseSalary, bonus},
 *      收入总和为baseSalary*13+bonus,以收入总和为排序标准。
 *      排序输出 年薪最高的100个人，输出结果为 xxxx:yyyy万
 * 第二：
 *      第五题中的 storeByteArry改为int[]数组，采用Java位操作方式来实现1个Int 拆解为4个Byte，存放MyItem对象的属性。
 *
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/2 22:35
 */
public class Practice07 {
    // 加分题01：快速排序
    @Test
    public void quickSortTest() {
        int[] arr = {6, 8, 1, 3, 2, 6, 4};
        System.out.println("排序前：" + Arrays.toString(arr));
        QuickSort.sort(arr);
        System.out.println("排序后：" + Arrays.toString(arr));
    }

    // 加分题01：冒泡排序
    @Test
    public void bubblingTest() {
        int[] arr = {6, 8, 1, 3, 2, 6, 4};
        System.out.println("排序前：" + Arrays.toString(arr));
        bubblingSort(arr);
        System.out.println("排序后：" + Arrays.toString(arr));
    }

    public void bubblingSort(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                int temp = arr[i];
                if (temp > arr[j]) {
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    // 加分题01：对 salary使用快速排序
    @Test
    public void salaryQuickSort() {
        Salary[] salaries = Practice_04.mockData();
        QuickSortUtil.log = false;
        QuickSortUtil.sort(salaries, (s1, s2) -> {
            int s1Total = s1.getBaseSalary() * 13 + s1.getBonus();
            int s2Total = s2.getBaseSalary() * 13 + s2.getBonus();

            if (s1Total < s2Total) return 1;
            if (s1Total == s2Total) return 0;
            return -1;
        });
        printTopN(salaries, 100);
    }

    // 加分题01：对 salary使用冒泡排序
    @Test
    public void salaryBubbling() {
        Salary[] salaries = Practice_04.mockData();
        bubblingSort(salaries, (s1, s2) -> {
            int s1Total = s1.getBaseSalary() * 13 + s1.getBonus();
            int s2Total = s2.getBaseSalary() * 13 + s2.getBonus();

            if (s1Total < s2Total) return 1;
            if (s1Total == s2Total) return 0;
            return -1;
        });
        printTopN(salaries, 100);
    }

    private void printTopN(Salary[] salaries, int topN) {
        Salary[] top100 = Arrays.copyOf(salaries, topN);
        for (Salary salary : top100) {
            System.out.println(salary.getName() + " : " + (salary.getBaseSalary() * 13 + salary.getBonus()));
        }
    }

    private <T> void bubblingSort(T[] arr, Comparator<? super T> c) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                T t1 = arr[i];
                T t2 = arr[j];
                int compare = c.compare(t1, t2);
                if (compare > 0) {
                    arr[i] = t2;
                    arr[j] = t1;
                }
            }
        }
    }

    // 对第5题进行改写后的测试
    @Test
    public void storeByteTest() {
        ByteStore byteStore = new ByteStore();
        MyItem item = new MyItem((byte) 1, (byte) 128, (byte) 3);
        byteStore.putMyItem(0, item);
        System.out.println(item);
        MyItem myItem = byteStore.getMyItem(0);
        System.out.println(myItem);
        System.out.println(item.equals(myItem));
        System.out.println("---------------------");

        item = new MyItem((byte) 2, (byte) 3, (byte) 4);
        byteStore.putMyItem(1, item);
        System.out.println(item);
        myItem = byteStore.getMyItem(1);
        System.out.println(myItem);
        System.out.println(item.equals(myItem));
        System.out.println("---------------------");

        item = new MyItem((byte) 5, (byte) -9, (byte) -4);
        byteStore.putMyItem(2, item);
        System.out.println(item);
        myItem = byteStore.getMyItem(2);
        System.out.println(myItem);
        System.out.println(item.equals(myItem));
        System.out.println("---------------------");
    }
}
