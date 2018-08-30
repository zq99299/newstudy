package cn.mrcode.newstudy.design.pattern.creational.builder;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/30 22:31
 */

public class DirectorBoss {
    private Builder builder;

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public Computer createComputer(
            String cpu,
            String mainBoard,
            String hardDisk,
            String displayCard,
            String power,
            String memory
    ) {
        builder.buildCPU(cpu);
        builder.buildMainBoard(mainBoard);
        builder.buildHardDisk(hardDisk);
        builder.buildDisplayCard(displayCard);
        builder.buildPower(power);
        builder.buildMemory(memory);
        return builder.createComputer();
    }
}
