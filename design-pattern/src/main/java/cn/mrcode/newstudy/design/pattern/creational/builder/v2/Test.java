package cn.mrcode.newstudy.design.pattern.creational.builder.v2;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/30 22:54
 */
public class Test {
    public static void main(String[] args) {
        Computer computer = new Computer.ComputerBuilder()
                .builderCpu("酷睿I7")
                .builderDisplayCard("七彩虹显卡")
                .builder();
        System.out.println(computer);
    }
}
