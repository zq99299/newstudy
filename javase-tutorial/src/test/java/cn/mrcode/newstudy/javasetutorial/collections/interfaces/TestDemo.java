package cn.mrcode.newstudy.javasetutorial.collections.interfaces;

import org.junit.Test;

import java.util.*;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/09     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/9 11:33
 * @date 2017/12/9 11:33
 * @since 1.0.0
 */
public class TestDemo {
    // 编写一个以随机顺序打印参数的程序。不要复制参数数组。
    // 演示如何使用流和传统的增强for语句打印出元素。
    @Test
    public void fun1() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};

        List<Integer> ints = Arrays.asList(arr);
        Collections.shuffle(ints);

        // for-each 打印
        for (Integer i : ints) {
            System.out.print(i + " ");
        }
        System.out.println();
        // jdk8 流打印
        ints.stream().forEach(i -> System.out.print(i + " "));
    }

    @Test
    public void fun2() {
        String[] args = new String[]{"10x", "10X", "2", "3", "4", "5", "6", "7", "8", "9"};
        Set<String> s = new HashSet<String>();
        for (String a : args)
            s.add(a);
        System.out.println(s.size() + " distinct words: " + s);
        s = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.equalsIgnoreCase(o2) ? -1 : 1;
            }
        });
        for (String a : args)
            s.add(a);
        System.out.println(s.size() + " distinct words: " + s);
    }

    @Test
    public void fun3() {
        String[] args = new String[]{"10x", "10X", "2", "3 ", "4", " 5", "6", "7", "8", "9 "};
        List<String> strings = Arrays.asList(args);
        // 普通for
        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, strings.get(i).trim());
        }

        // list迭代器
        ListIterator<String> it = strings.listIterator();
        while (it.hasNext()) {
            it.set(it.next().trim());
        }
    }

    @Test
    public void fun4() {
        System.out.println(Integer.toHexString(16));
        System.out.println(Integer.toHexString(65));
    }

    @Test
    public void fun5(){
        //integers
        byte largestByte = Byte.MIN_VALUE;
        short largestShort = Short.MIN_VALUE;
        int largestInteger = Integer.MIN_VALUE;
        long largestLong = Long.MIN_VALUE;

        //real numbers
        float largestFloat = Float.MIN_VALUE;
        double largestDouble = Double.MIN_VALUE;
    }
}
