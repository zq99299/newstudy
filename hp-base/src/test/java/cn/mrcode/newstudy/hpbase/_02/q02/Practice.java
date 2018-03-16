package cn.mrcode.newstudy.hpbase._02.q02;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * <pre>
 *  分别用大头和小头模式将整数 a=10240写入到文件中（4个字节），并且再正确读出来，
 *  打印到屏幕上，同时截图UltraEdit里的二进制字节序列，做对比说明
 *
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/6 21:40
 */
public class Practice {
    // 大小头读取测试
    @Test
    public void main() {
        int a = 1000;

        System.out.println("---- big endian");
        byte[] bigItem = BitsUtil.convertBigItem(a);
        System.out.println(Arrays.toString(bigItem));
        System.out.println(BitsUtil.getBigItem(bigItem));

        System.out.println("---- little endian");
        byte[] littleItem = BitsUtil.convertLittleItem(a);
        System.out.println(Arrays.toString(littleItem));
        System.out.println(BitsUtil.getLittleItem(littleItem));
    }

    // 文件写入测试
    @Test
    public void writeFile() throws IOException {
        int a = 10240;
        byte[] bigItem = BitsUtil.convertBigItem(a);
        byte[] littleItem = BitsUtil.convertLittleItem(a);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(bigItem, 0, bigItem.length);
        os.write("\r\n".getBytes());
        os.write(littleItem, 0, littleItem.length);
        // 获取到的路径在out目录下
        os.writeTo(new FileOutputStream(Practice.class.getResource("/_02/q02/x").getFile()));
        os.flush();
        os.close();
    }
}
