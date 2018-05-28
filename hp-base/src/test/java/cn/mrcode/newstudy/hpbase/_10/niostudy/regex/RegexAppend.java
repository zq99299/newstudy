package cn.mrcode.newstudy.hpbase._10.niostudy.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 感觉和replaceAll效果类似，具体有什么用途呢
 */
public class RegexAppend {
    public static void main(String[] argv) {
        String input = "Thanks, thanks very much";
        String regex = "([Tt])hanks";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        StringBuffer sb = new StringBuffer();
        //循环直到遇到匹配
        while (matcher.find()) {
            if (matcher.group(1).equals("T")) {
                matcher.appendReplacement(sb, "Thank you");
            } else {
                matcher.appendReplacement(sb, "thank you");
            }
        }
        // 完成到StringBuffer的传送
        matcher.appendTail(sb);
        // 打印结果
        System.out.println(sb.toString());
        // 让我们再试试在替换中使用$n转义
        sb.setLength(0);
        matcher.reset();
        String replacement = "$1hank you";
        //循环直到遇到匹配
        while (matcher.find()) {
            matcher.appendReplacement(sb, replacement);
        }
        // 完成到StringBuffer传送
        matcher.appendTail(sb);
        // 打印结果
        System.out.println(sb.toString());
        // 再来一次，简单的方法（因为这个父子很简单）174
        System.out.println(matcher.replaceAll(replacement));
        // 最后一次，只使用字符串
        System.out.println(input.replaceAll(regex, replacement));
    }
}
