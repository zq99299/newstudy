package cn.mrcode.newstudy.hpbase._10.niostudy.regex;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/05/28     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/5/28 15:12
 * @date 2018/5/28 15:12
 * @since 1.0.0
 */
public class TestDemo {
    @Test
    public void fun1() {
        // 寻找字符串里面的空格
        Pattern pattern = Pattern.compile(" ");
        Matcher matcher = pattern.matcher("");
        String[] lines = new String[]{
                "abcde fg",
                "abc de fg",
                "abc de fggggg",
                "fggggg"
        };
        for (String line : lines) {
            matcher.reset(line);
            if (matcher.find()) {
                System.out.println(line);
            }
        }
    }

    @Test
    public void fun2() {
        String regex = "A((B)(C(D)))";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher("");
        matcher.groupCount();
    }

    // 追加
    @Test
    public void fun3() {
        // 与普通的
        Pattern pattern = Pattern.compile("([Tt])hanks");
        Matcher matcher = pattern.matcher("Thanks, thanks very much");
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            if (matcher.group(1).equals("T")) {
                matcher.appendReplacement(sb, "Thank you");
            } else {
                matcher.appendReplacement(sb, "thank you");
            }
        }
        // 未配匹配的当前位置的所有剩余字符将被添加
        matcher.appendTail(sb);
        // Thank you, thank you very much
        System.out.println(sb);
    }

    public void reges() {

    }


}
