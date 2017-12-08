package cn.mrcode.newstudy.javasetutorial;

import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ${desc}
 * @author zhuqiang
 * @version 1.0.1 2017/12/8 10:32
 * @date 2017/12/8 10:32
 * @since 1.0
 */
public class TestTest {
    @Test
    public void fun1() {
        List<Color> myShapesCollection = Arrays.asList(Color.BLUE, Color.RED, Color.BLACK);
        myShapesCollection.stream()
                .filter(e -> e.getRGB() == Color.RED.getRGB())
                .forEach(e -> System.out.println(e.getRGB()));

        String joined = myShapesCollection.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        System.out.println(joined);
    }
}