package cn.mrcode.newstudy.design.pattern.principle.demeter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 14:48
 */
public class TeamLeader {
    public void checkNumber() {
        // 老板获取到所有的课程
        List<Course> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new Course());
        }
        System.out.println("在线课程数量是：" + list.size());
    }
}
