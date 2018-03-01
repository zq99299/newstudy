package cn.mrcode.newstudy.hpbase._01.q05;

import java.util.Arrays;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/1 22:30
 */
public class ByteStore {
    private static int MAX_SIZE = 1000;
    private byte[] store;
    // 此容器长度
    private int size;

    public ByteStore() {
        store = new byte[MAX_SIZE * 3];
    }

    public void putMyItem(int index, MyItem item) {
        byte type = item.getType();
        byte color = item.getColor();
        byte price = item.getPrice();

        // 获取当前索引对应的store中一个完整数据的最后一个索引
        /*
            ByteStore.index 0 : store-0,1,2
            ByteStore.index 1 : store-3,4,5
            ByteStore.index 3 : store-6,7,8
         */
        int endIndex = getEndIndex(index);
        store[endIndex] = price;
        store[endIndex - 1] = color;
        store[endIndex - 2] = type;

        size++;
    }

    private int getEndIndex(int index) {
        if (index == 0) {
            return 2;
        }
        if (index == MAX_SIZE - 1) {
            return index * 3 - 1;
        }
        return index * 3 + 2;
    }


    public MyItem getMyItem(int index) {
        int endIndex = getEndIndex(index);
        byte type = store[endIndex - 2];
        byte color = store[endIndex - 1];
        byte price = store[endIndex];
        // 这里返回的是新对象，内存地址肯定不会是同一个了
        return new MyItem(type, color, price);
    }

    public void sort() {
        /**
         * 对store排序，相当于是对 ByteStore 这个数据容器的元素（myItem）进行排序
         * 直接对 store 排序 想不到更好的办法
         * 只能使用以下的笨方法了
         */
        MyItem[] myItems = new MyItem[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++) {
            myItems[i] = getMyItem(i);
        }
        Arrays.sort(myItems, (m1, m2) -> {
            byte price1 = m1.getPrice();
            byte price2 = m2.getPrice();
            if (price1 == price2) return 0;
            if (price1 < price2) return 1;
            return -1;
        });
        for (int i = 0; i < myItems.length; i++) {
            putMyItem(i, myItems[i]);
        }
    }
}
