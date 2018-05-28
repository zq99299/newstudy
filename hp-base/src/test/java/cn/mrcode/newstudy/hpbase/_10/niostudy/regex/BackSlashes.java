package cn.mrcode.newstudy.hpbase._10.niostudy.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用反斜杠
 */
public class BackSlashes {
    public static void main(String[] argv) {
        // 在输入中把XYZ或ABC替换成“a\b”
        String rep = "a\\\\b";
        String input = "> XYZ <=> ABC <";
        Pattern pattern = Pattern.compile("ABC|XYZ");
        Matcher matcher = pattern.matcher(input);
        System.out.println(matcher.replaceFirst(rep));
        System.out.println(matcher.replaceAll(rep));
        // 把所有的\n 替换成\r\n， DOS-like CR/LF
        rep = "\\\\r\\\\n";
        input = "line 1\nline 2\nline 3\n";
        pattern = Pattern.compile("\\n");
        matcher = pattern.matcher(input);
        System.out.println("");
        System.out.println("Before:");
        System.out.println(input);
        System.out.println("After (dos-ified, escaped):");
        System.out.println(matcher.replaceAll(rep));
    }
}
