package cn.mrcode.newstudy.design.pattern.principle.singleresponsibility;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 13:34
 */
public class Method {
    public void updateUserInfo(String userName, String address, boolean bool) {
        if (bool) {
            // 逻辑1
        } else {
            // 逻辑2
        }
    }
}
