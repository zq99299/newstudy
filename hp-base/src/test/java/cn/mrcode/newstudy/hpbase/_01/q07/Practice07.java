package cn.mrcode.newstudy.hpbase._01.q07;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/2 22:35
 */
public class Practice07 {
    // 快速排序
    @Test
    public void quickSortTest() {
        int[] arr = {6, 8, 1, 3, 2, 6, 4};
        System.out.println("排序前：" + Arrays.toString(arr));
        QuickSort.sort(arr);
        System.out.println("排序后：" + Arrays.toString(arr));
    }

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
}
