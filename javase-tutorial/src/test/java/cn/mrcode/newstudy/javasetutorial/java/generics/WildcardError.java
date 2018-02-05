package cn.mrcode.newstudy.javasetutorial.java.generics;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/19     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/19 14:33
 * @date 2017/12/19 14:33
 * @since 1.0.0
 */
import java.util.List;

public class WildcardError {

    void foo(List<?> i) {
//        i.set(0, i.get(0).var);
    }
}
