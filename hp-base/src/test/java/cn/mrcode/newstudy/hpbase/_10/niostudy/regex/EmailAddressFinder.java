package cn.mrcode.newstudy.hpbase._10.niostudy.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 复杂的正则表达式。但是看不懂。好复杂
 */
public class EmailAddressFinder {
    public static void main(String[] argv) {
        argv = new String[]{
                "ron@ronsoft.com",
                "barney@rubble.org",
                "<wflintstone@rockvegas.com>",
                "fred@bedrock.com"
        };
        if (argv.length < 1) {
            System.out.println("usage: emailaddress ...");
        }
        // 编译电子邮件地址检测器的模式
        Pattern pattern = Pattern.compile(
                "([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]"
                        + "{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))"
                        + "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)",
                Pattern.MULTILINE);
        // 为模式制作一个Matcher对象
        Matcher matcher = pattern.matcher("");
        // 循环遍历参数并在每个寻找地址
        for (int i = 0; i < argv.length; i++) {
            boolean matched = false;
            System.out.println("");
            System.out.println("Looking at " + argv[i] + " ...");
            // 将匹配器复位，查看当前实参字符串
            matcher.reset(argv[i]);
            // 遇到匹配时循环
            while (matcher.find())
                // 找到一个匹配
                System.out.println("\t" + matcher.group());
            matched = true;

            if (!matched) {
                System.out.println("\tNo email addresses found");
            }
        }
    }
}
