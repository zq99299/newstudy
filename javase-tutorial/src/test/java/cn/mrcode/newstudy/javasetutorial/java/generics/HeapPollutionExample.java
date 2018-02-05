package cn.mrcode.newstudy.javasetutorial.java.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/20     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/20 13:40
 * @date 2017/12/20 13:40
 * @since 1.0.0
 */
public class HeapPollutionExample {
    public static void main(String[] args) {

        List<String> stringListA = new ArrayList<String>();
        List<String> stringListB = new ArrayList<String>();

        ArrayBuilder.addToList(stringListA, "Seven", "Eight", "Nine");
        ArrayBuilder.addToList(stringListB, "Ten", "Eleven", "Twelve");
        List<List<String>> listOfStringLists =
                new ArrayList<List<String>>();
        ArrayBuilder.addToList(listOfStringLists,
                               stringListA, stringListB);

        ArrayBuilder.faultyMethod(Arrays.asList("Hello!"), Arrays.asList("World!"));
    }
}
