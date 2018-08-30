package cn.mrcode.newstudy.design.pattern.creational.builder;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/30 22:27
 */
public class Computer {
    private String CPU;
    private String mainBoard;
    private String hardDisk;
    private String displayCard;
    private String power;
    private String memory;

    @Override
    public String toString() {
        return "Computer{" +
                "CPU='" + CPU + '\'' +
                ", mainBoard='" + mainBoard + '\'' +
                ", hardDisk='" + hardDisk + '\'' +
                ", displayCard='" + displayCard + '\'' +
                ", power='" + power + '\'' +
                ", memory='" + memory + '\'' +
                '}';
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public String getCPU() {
        return CPU;
    }

    public void setMainBoard(String mainBoard) {
        this.mainBoard = mainBoard;
    }

    public String getMainBoard() {
        return mainBoard;
    }

    public void setHardDisk(String hardDisk) {
        this.hardDisk = hardDisk;
    }

    public String getHardDisk() {
        return hardDisk;
    }

    public void setDisplayCard(String displayCard) {
        this.displayCard = displayCard;
    }

    public String getDisplayCard() {
        return displayCard;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getPower() {
        return power;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getMemory() {
        return memory;
    }
}
