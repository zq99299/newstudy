package cn.mrcode.newstudy.design.pattern.creational.builder;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/30 22:36
 */
public class Test {
    public static void main(String[] args) {
        DirectorBoss boss = new DirectorBoss();
        boss.setBuilder(new ActualBuilder());
        Computer computer = boss.createComputer(
                "酷睿I7",
                "华硕主板",
                "三星硬盘",
                "七彩虹显卡",
                "金河田电源",
                "万紫千红内存"
        );
        System.out.println(computer);
    }
}
