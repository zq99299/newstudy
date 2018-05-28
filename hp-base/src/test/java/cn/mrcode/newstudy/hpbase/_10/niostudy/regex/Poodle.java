package cn.mrcode.newstudy.hpbase._10.niostudy.regex;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 根据模式拆分字符串：测试第二个参数
 */
public class Poodle {
    /**
     * Generate a matrix table of how Pattern.split( ) behaves with
     * various regex patterns and limit values.
     */
    public static void main(String[] argv)
            throws Exception {
        String input = "poodle zoo";
        Pattern space = Pattern.compile(" ");
        Pattern d = Pattern.compile("d");
        Pattern o = Pattern.compile("o");
        Pattern[] patterns = {space, d, o};
        int limits[] = {1, 2, 5, -2, 0};
// 如果有参数就使用提供的参数。假设参数合理。
// 用法：输入模式[pattern ...]
// 别忘了引用参数。
        if (argv.length != 0) {
            input = argv[0];
            patterns = collectPatterns(argv);
        }
        generateTable(input, patterns, limits);
    }

    /**
     * Output a simple XML document with the results of applying
     * the list of regex patterns to the input with each of the
     * limit values provided. I should probably use the JAX APIs
     * to do this, but I want to keep the code simple.
     */
    private static void generateTable(String input,
                                      Pattern[] patterns, int[] limits) {
        System.out.println("<?xml version='1.0'?>");
        System.out.println("<table>");
        System.out.println("\t<row>");
        System.out.println("\t\t<head>Input: "
                                   + input + "</head>");
        for (int i = 0; i < patterns.length; i++) {
            Pattern pattern = patterns[i];
            System.out.println("\t\t<head>Regex: <value>"
                                       + pattern.pattern() + "</value></head>");
        }
        System.out.println("\t</row>");
        for (int i = 0; i < limits.length; i++) {
            int limit = limits[i];
            System.out.println("\t<row>");
            System.out.println("\t\t<entry>Limit: "
                                       + limit + "</entry>");
            for (int j = 0; j < patterns.length; j++) {
                Pattern pattern = patterns[j];
                String[] tokens = pattern.split(input, limit);
                System.out.print("\t\t<entry>");
                for (int k = 0; k < tokens.length; k++) {
                    System.out.print("<value>"
                                             + tokens[k] + "</value>");
                }
                System.out.println("</entry>");
            }
            System.out.println("\t</row>");
        }
        System.out.println("</table>");
    }

    /**
     * If command line args were given, compile all args after the
     * first as a Pattern. Return an array of Pattern objects.
     */
    private static Pattern[] collectPatterns(String[] argv) {
        List list = new LinkedList();
        for (int i = 1; i < argv.length; i++) {
            list.add(Pattern.compile(argv[i]));
        }
        Pattern[] patterns = new Pattern[list.size()];
        list.toArray(patterns);
        return (patterns);
    }
}
