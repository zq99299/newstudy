package cn.mrcode.newstudy.javasetutorial.java.generics;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/20     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/20 15:27
 * @date 2017/12/20 15:27
 * @since 1.0.0
 */
public class Algorithm {
    public static void main(String[] args) {
        Collection<Integer> ci = Arrays.asList(1, 2, 3, 4);
        int count = Algorithm.countIf(ci, new OddPredicate());
        System.out.println("Number of odd integers = " + count);
    }

    public static <T> int countIf(Collection<T> c, UnaryPredicate up) {
        int count = 0;
        for (T t : c) {
            // 可以看到通过接口操作把判定逻辑放到了 传递进来的实例中
            if (up.test(t)) {
                count++;
            }
        }
        return count;
    }

    public static <T>
    int findFirst(List<T> list, int begin, int end, UnaryPredicate<T> p) {

        for (; begin < end; ++begin)
            if (p.test(list.get(begin)))
                return begin;
        return -1;
    }

    // x > 0 and y > 0
    public static int gcd(int x, int y) {
        for (int r; (r = x % y) != 0; x = y, y = r) {
        }
        return y;
    }

    // 一元操作接口
    interface UnaryPredicate<T> {
        boolean test(T t);
    }

    // 奇数操作
    static class OddPredicate implements UnaryPredicate<Integer> {

        @Override
        public boolean test(Integer val) {
            return val % 2 != 0;
        }
    }
}
