package cn.mrcode.newstudy.hpbase.mysql.mymysql;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/26     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/26 17:26
 * @date 2018/6/26 17:26
 * @since 1.0.0
 */
public class SecurityutilTest {
    @Test
    public void scramble411Test() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] seed = "123456789".getBytes();
        byte[] pass = "987654321".getBytes();
        System.out.println(Arrays.toString(scramble411(pass, seed)));
    }

    public static final byte[] scramble411(byte[] pass, byte[] seed) throws NoSuchAlgorithmException {
        // 加密算法
        // SHA1( password ) XOR SHA1( "20-bytes random data from server" <concat> SHA1( SHA1( password ) ) )
        // 20 字节的随机字符 来自服务器；也就是这里的authPluginDataPart1 + authPluginDataPart2
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] pass1 = md.digest(pass);
        md.reset();
        byte[] pass2 = md.digest(pass1);
        md.reset();
        md.update(seed);  // update 的作用就相当于追加内容了。看效果
        byte[] pass3 = md.digest(pass2);
        for (int i = 0; i < pass3.length; i++) {
            pass3[i] = (byte) (pass3[i] ^ pass1[i]);
        }
        return pass3;
    }
}
