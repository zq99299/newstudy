package cn.mrcode.newstudy.hpbase._01.q05;

import org.junit.Test;

import java.util.Random;

/**
 * <pre>
 * 编码实现下面的要求
 * 现有对象 MyItem {byte type,byte color,byte price} ，要求将其内容存放在一个扁平的byte[]数组存储数据的ByteStore {byte[] storeByteArry}对象里,即每个MyItem占用3个字节，第一个MyItem占用storeByteArry[0]-storeByteArry[2] 3个连续字节，以此类推，最多能存放1000个MyItem对象
 * ByteStore提供如下方法
 *  - putMyItem(int index,MyItem item) 在指定的Index上存放MyItem的属性，这里的Index是0-999，而不是storeByteArry的Index
 *  - getMyItem(int index),从指定的Index上查找MyItem的属性，并返回对应的MyItem对象。
 *
 * 要求放入3个MyItem对象（index为0-2）并比较getMyItem方法返回的这些对象是否与之前放入的对象equal。
 *
 * 加分功能如下：
 * 放入1000个MyItem对象到ByteStore中，采用某种算法对storeByteArry做排序，排序以price为基准，排序完成后，输出前100个结果
 *
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/1 22:28
 */
public class Practice05 {
    @Test
    public void main() {
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

    // 加分功能
    @Test
    public void fun2() {
        ByteStore byteStore = new ByteStore();
        for (int i = 0; i < 1000; i++) {
            byteStore.putMyItem(i, new MyItem(buildRandom(), buildRandom(), buildRandom()));
        }

        byteStore.sort();

        for (int i = 0; i < 100; i++) {
            System.out.println(byteStore.getMyItem(i));
        }
    }

    private Random random = new Random();

    private byte buildRandom() {
        return (byte) random.nextInt(128);
    }
}
