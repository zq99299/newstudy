package cn.mrcode.newstudy.hpbase._10.niostudy.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式替换示例
 */
public class RegexReplace {
    /**
     * 0: 正则
     * 1：替换的字符
     * 3- n. 被替换的字符
     * @param argv
     */
    public static void replace(String[] argv) {
        // 完整性检查，至少需要三个参数
        if (argv.length < 3) {
            System.out.println("usage: regex replacement input ...");
            return;
        }
        // 用助词符号名保存正则及替换字符串170
        String regex = argv[0];
        String replace = argv[1];
        // 编译表达式；一次只能编译一个
        Pattern pattern = Pattern.compile(regex);
        // 得到Matcher实例， 暂时先使用虚设的输入字符串
        Matcher matcher = pattern.matcher("");
        // 打印输出用于参考
        System.out.println(" regex: '" + regex + "'");
        System.out.println(" replacement: '" + replace + "'");
        // 对各个剩余的参数字符串应用正则/替换
        for (int i = 2; i < argv.length; i++) {
            System.out.println("------------------------");
            matcher.reset(argv[i]);
            System.out.println(" input: '" + argv[i] + "'");
            System.out.println(" replaceFirst( ): '" + matcher.replaceFirst(replace) + "'");
            System.out.println(" replaceAll( ): '" + matcher.replaceAll(replace) + "'");
        }
    }

    public static void main(String[] args) {
        String[] argv = {"a*b", "-", "aabfooaabfooabfoob"};
        replace(argv);
        // 空白字符
        argv = new String[]{"\\p{Blank}", "-", "fee fie foe fum"};
        replace(argv);
        argv = new String[]{"([bB])yte", "$1", "Byte for byte"};
        replace(argv);
        // 匹配4个字符加-的字符串，并替换
        argv = new String[]{"\\d\\d\\d\\d([-])", "xxxx$1", "card #1234-5678-1234"};
        replace(argv);
    }
}
