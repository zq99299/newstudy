package cn.mrcode.newstudy.javasetutorial.java.generics;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/20     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/20 13:38
 * @date 2017/12/20 13:38
 * @since 1.0.0
 */
public class ArrayBuilder {
    public static <T> void addToList(List<T> listArg, T... elements) {
        for (T x : elements) {
            listArg.add(x);
        }
    }

    public static void faultyMethod(List<String>... l) {
        Object[] objectArray = l; // Valid
        objectArray[0] = Arrays.asList(42);
        String s = l[0].get(0); // ClassCastException thrown here
    }
}
