package cn.mrcode.newstudy.design.pattern.creational.builder;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/30 22:23
 */
// 建造者接口
public abstract class Builder {
    public abstract Computer createComputer();

    public abstract void buildCPU(String cpu);

    public abstract void buildMainBoard(String mainBoard);

    public abstract void buildHardDisk(String hardDisk);

    public abstract void buildDisplayCard(String displayCard);

    public abstract void buildPower(String power);

    public abstract void buildMemory(String memory);
}
