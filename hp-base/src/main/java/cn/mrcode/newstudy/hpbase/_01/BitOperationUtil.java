package cn.mrcode.newstudy.hpbase._01;

/**
 * 位运算操作工具类
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/02/03     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/2/3 16:14
 * @date 2018/2/3 16:14
 * @since 1.0.0
 */
public class BitOperationUtil {
    /**
     * 把data转换为无符号数字；
     * @param data
     * @return 返回 0 - 255 的数字
     */
    public static int getUnsignedByte(byte data) {
        // data & 1111 1111;
        // 只有 1 & 1 = 1;
        /**
         * 1000 0010  -2的源码
         * 1111 1101  -2的反码
         * 111111111111111111111111 1111 1110  -2的补码
         * 00000000000000000000000 1111 1111
         * ----------------------------------
         * 0000000000000000000000 1111 1110
         */
        // 要记住一点，而且是非常重要：位运算是针对整型的，进行位操作时，除long外，其他的类型会自动转成int
        // 所以这里为什么 & 运算之后，一个负数就变成了正数。
        // 这里我按照8位来算，始终都没有搞明白为什么8位的开头是1，却是一个正数
        // java 中的类似是有符号的
//        return Byte.toUnsignedInt(data);
        return data & 0xFF;
    }

    public static void main(String[] args) {
        System.out.println((byte) 254);

        // 01101000
        byte head = (byte) 104;
        // 使用按位与 把第三和第4位提取出来
        int x = head & 0b0011_0000;
        // 把第5位开始的都移走，让目标数字变成最低位
        // 由于上面使用了按位与，所以把不想关的位都变成了0
        // 这里再使用位移操作就得到了我们想要的值
        System.out.println(x >>> 4); // 十进制2

    }
}
