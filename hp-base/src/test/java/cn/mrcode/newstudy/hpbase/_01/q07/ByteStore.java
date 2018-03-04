package cn.mrcode.newstudy.hpbase._01.q07;

import cn.mrcode.newstudy.hpbase._01.q05.MyItem;

/**
 * 第五题中的 storeByteArry改为int[]数组，采用Java位操作方式来实现1个Int 拆解为4个Byte，存放MyItem对象的属性。
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/4 10:46
 */
public class ByteStore {
    private static int MAX_SIZE = 1000;
    private int[] store = new int[MAX_SIZE];
    // 此容器当前长度
    private int size;

    public void putMyItem(int index, MyItem item) {
        byte type = item.getType();
        byte color = item.getColor();
        byte price = item.getPrice();
        /*
            用int来存储三个byte，示意图如下
            00000000 00000000 00000000 00000000 一个int4个8位，32bit
                       type    color   price
            使用位移方式把byte移动到指定位置
         */
        int e = (type << 16) + (color << 8) + price;
        store[size++] = e;
    }

    public MyItem getMyItem(int index) {
        int e = store[index];
        // 前面讲到过，获取指定位置的值
        // 需要通过与操作将其他不需要的位置重置为0
        // 然后通过位移操作把值移动到最（低位）右端
        // 必须注意位运算中的符号优先级问题
        byte type = (byte) ((e & 0x00FF0000) >>> 16);
        byte color = (byte) ((e & 0x0000FF00) >>> 8);
        byte price = (byte) e;  // 强转会直接丢弃最高位，保留范围类的最大值
        return new MyItem(type, color, price);
    }
}
