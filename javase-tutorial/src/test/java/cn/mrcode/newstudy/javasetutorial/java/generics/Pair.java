package cn.mrcode.newstudy.javasetutorial.java.generics;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/15     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/15 13:44
 * @date 2017/12/15 13:44
 * @since 1.0.0
 */
public class Pair<K, V> {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}