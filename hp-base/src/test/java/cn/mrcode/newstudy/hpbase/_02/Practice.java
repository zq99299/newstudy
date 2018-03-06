package cn.mrcode.newstudy.hpbase._02;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * 第02课
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/6 21:33
 */
public class Practice {
    /**
     * <pre>
     *  得到 String s="中国" 这个字符串的utf-8编码，gbk编码，iso-8859-1编码的字符串，
     *   看看各自有多少字节，
     *   同时解释为什么以utf-8编码得到的byte[]无法用gbk的方式“还原”为原来的字符串
     * </pre>
     */
    @Test
    public void q1() throws UnsupportedEncodingException {
        String s = "中国";
        System.out.println("UTF8:" + s.getBytes("UTF-8").length);
        System.out.println("GBK:" + s.getBytes("GBK").length);
        System.out.println("ISO88581:" + s.getBytes("ISO-8859-1").length);
    }
}
