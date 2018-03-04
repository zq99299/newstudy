package cn.mrcode.newstudy.hpbase._01.q07;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 快速排序
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/2 23:08
 */
public class QuickSortUtil {
    public static boolean log = true;

    public static <T> void sort(T[] arr, Comparator<? super T> c) {
        if (arr.length > 0) {
            sort(arr, c, 0, arr.length - 1);
        }
    }

    /**
     * @param arr
     * @param lowIndex   低位
     * @param hightIndex 高位
     */
    private static <T> void sort(T[] arr, Comparator<? super T> c, int lowIndex, int hightIndex) {
        if (lowIndex < hightIndex) {
            int middle = getMiddleAndSwap(arr, c, lowIndex, hightIndex);
            sort(arr, c, lowIndex, middle - 1);
            sort(arr, c, middle + 1, hightIndex);
        }
    }

    /**
     * 获取分界点索引，并把大小分开到分界点两端
     * @param arr
     * @param lowIndex
     * @param hightIndex
     * @return
     */
    private static <T> int getMiddleAndSwap(T[] arr, Comparator<? super T> c, int lowIndex, int hightIndex) {
        /**
         * 1. 从低位找一个基准值
         * 2. 从高位开始降低对比找到比基准值还小的数。然后把这个数放到当前的低位
         * 3. 从低位开始增高对比找到比基准值还大的数。然后把这个数放到当前的高位
         * 4. 把当前的低位放入基准值。 这个就是一个分界点，左边的小于基准值。右边的大于基准值
         * 5. 把左右两拨分开递归重复上述1，2，3，4步骤
         */
        T base = arr[lowIndex]; // 基准值
        while (lowIndex < hightIndex) {
            if (log) {
                System.out.println("-------------------- 基准：" + base);
                System.out.println("找数之前：" + Arrays.toString(arr));
            }
            // / 找到比基准值小的数
            while (lowIndex < hightIndex && c.compare(arr[hightIndex], base) >= 0) {
                hightIndex--;
            }
            // 找到比基准值小的数，然后把基准所在的位置 设置上这个小的值
            // 要记得最开始已经把基准索引的值保存为 base了
            arr[lowIndex] = arr[hightIndex];
            if (log) System.out.println("找到小数：" + Arrays.toString(arr));
            // 然后找到比基准大的数
            while (lowIndex < hightIndex && c.compare(arr[lowIndex], base) <= 0) {
                lowIndex++;
            }

            // 然后把高位的数设置为这个比基准大的数
            arr[hightIndex] = arr[lowIndex];
            if (log) System.out.println("找到大数：" + Arrays.toString(arr));
        }
        // 把当前位置的值设置为基准数
        arr[lowIndex] = base;
        if (log) System.out.println("填回枢轴：" + Arrays.toString(arr));
        return lowIndex;
    }
}
