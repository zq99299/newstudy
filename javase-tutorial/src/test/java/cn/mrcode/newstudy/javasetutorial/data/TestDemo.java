package cn.mrcode.newstudy.javasetutorial.data;

import org.junit.Test;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/09     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/9 15:26
 * @date 2017/12/9 15:26
 * @since 1.0.0
 */
public class TestDemo {
    @Test
    public void fun1() {
        String palindrome = "Dot saw I was Tod";
        int len = palindrome.length();
        char[] tempCharArray = new char[len];
        char[] charArray = new char[len];

        // put original string in an
        // array of chars
        for (int i = 0; i < len; i++) {
            tempCharArray[i] =
                    palindrome.charAt(i);
        }
        palindrome.getChars(0, len, tempCharArray, 0);

        // reverse array of chars
        for (int j = 0; j < len; j++) {
            charArray[j] =
                    tempCharArray[len - 1 - j];
        }

        String reversePalindrome =
                new String(charArray);
        System.out.println(reversePalindrome);
    }

    @Test
    public void fun2() {
        // 模拟从命令行启动的，传入了2个参数
        String[] args = new String[]{"4.5", "87.2"};
        // this program requires two
        // arguments on the command line
        if (args.length == 2) {
            // convert strings to numbers
            float a = (Float.valueOf(args[0])).floatValue();
            float b = (Float.valueOf(args[1])).floatValue();

            // 做一些算术
            System.out.println("a + b = " +
                                       (a + b));
            System.out.println("a - b = " +
                                       (a - b));
            System.out.println("a * b = " +
                                       (a * b));
            System.out.println("a / b = " +
                                       (a / b));
            System.out.println("a % b = " +
                                       (a % b));
        } else {
            System.out.println("This program " +
                                       "requires two command-line arguments.");
        }
    }

    @Test
    public void fun3() {
        final String FPATH = "/home/user/index.html";
        Filename myHomePage = new Filename(FPATH, '/', '.');
        System.out.println("Extension = " + myHomePage.extension());
        System.out.println("Filename = " + myHomePage.filename());
        System.out.println("Path = " + myHomePage.path());
    }
}
