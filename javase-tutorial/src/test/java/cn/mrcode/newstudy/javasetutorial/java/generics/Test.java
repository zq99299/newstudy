package cn.mrcode.newstudy.javasetutorial.java.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/15     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/15 14:11
 * @date 2017/12/15 14:11
 * @since 1.0.0
 */
public class Test {
    static <T> T pick(T a1, T a2) {
        return a2;
    }


    @org.junit.Test
    public void ss() {
        List<EvenNumber> le = new ArrayList<>();
        List<? extends NaturalNumber> ln = new ArrayList<EvenNumber>();
//        ln.add(new NaturalNumber(35)); // compile-time error
    }

    class MyClass<X> {
        <T> MyClass(T t) {
            // ...
        }
    }

    @org.junit.Test
    public void fun2() {
        MyNode mn = new MyNode(5);
        Node n = mn;            // A raw type - compiler throws an unchecked warning
        n.setData("Hello");
        Integer x = mn.data;    // 报错ClassCastException
    }

    @org.junit.Test
    public void fun3() {
//        Pair<Integer, char> p = new Pair<>(8, 'a');  // compile-time error
//
//        List<Integer> li = new ArrayList<>();
//        List<Number> ln = (List<Number>) li; // compile-time error
//        ArrayList<Integer> ll = (ArrayList<Integer>)li;
    }

    class Pair<K, V> {

        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        // ...
    }

    //    public static <E> void append(List<E> list) {
//        E elem = new E();  // compile-time error
//        list.add(elem);
//    }
    public static <E> void append(List<E> list, Class<E> cls) throws Exception {
        E elem = cls.newInstance();   // OK
        list.add(elem);
    }

//    public static <E> void rtti(List<E> list) {
//        if (list instanceof ArrayList<Integer>) {  // compile-time error
//            // ...
//        }
//    }

    public static void rtti(List<?> list) {
        if (list instanceof ArrayList<?>) {  // OK; instanceof requires a reifiable type
            // ...
        }
    }

    @org.junit.Test
    public void fun5() {

    }

    public static void print(List<? extends Number> list) {
        for (Number n : list)
            System.out.print(n + " ");
        System.out.println();
    }

    @org.junit.Test
    public void fun6() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 3);
        System.out.println(max(list, 0, list.size()));
        List<String> listStr = Arrays.asList("a", "v", "xk", "oooo", "z", "d");
        System.out.println(max(listStr, 0, list.size()));
    }

//    public static <T extends Comparable<T>> T max(List<T> list, int begin, int end) {
//        T max = null;
//        for (int i = begin; i < end; i++) {
//            T t = list.get(i);
//            if (max == null) {
//                max = t;
//                continue;
//            }
//            if (max.compareTo(t) < 0) {
//                max = t;
//            }
//        }
//        return max;
//    }

    public static <T extends Object & Comparable<? super T>>
    T max(List<? extends T> list, int begin, int end) {

        T maxElem = list.get(begin);

        for (++begin; begin < end; ++begin)
            if (maxElem.compareTo(list.get(begin)) < 0)
                maxElem = list.get(begin);
        return maxElem;
    }

/*    class Node<T> implements Comparable<T> {
        public int compareTo(T obj) { *//* ... *//* return 0}
    }

    @org.junit.Test
    public void fun7(){
        Node<String> node = new Node<>();
        Comparable<String> comp = node;
    }*/
}
