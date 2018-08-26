package cn.mrcode.newstudy.design.pattern.principle.singleresponsibility;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 13:04
 */
public class Test {
    public static void main(String[] args) {
//        Bird bird = new Bird();
//        bird.mainMoveMode("大雁");
//        bird.mainMoveMode("鸵鸟");

        FlyBird flyBird = new FlyBird();
        flyBird.mainMoveMode("大雁");

        WalkBird walkBird = new WalkBird();
        walkBird.mainMoveMode("鸵鸟");
    }
}
