package cn.mrcode.newstudy.design.pattern.creational.builder.v2;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/30 22:27
 */
public class Computer {
    private String cpu;
    private String mainBoard;
    private String hardDisk;
    private String displayCard;
    private String power;
    private String memory;

    public Computer(ComputerBuilder computerBuilder) {
        this.cpu = computerBuilder.cpu;
        this.mainBoard = computerBuilder.mainBoard;
        this.hardDisk = computerBuilder.hardDisk;
        this.displayCard = computerBuilder.displayCard;
        this.power = computerBuilder.power;
        this.memory = computerBuilder.memory;
    }

    public static class ComputerBuilder {
        private String cpu;
        private String mainBoard;
        private String hardDisk;
        private String displayCard;
        private String power;
        private String memory;

        public Computer builder() {
            return new Computer(this);
        }

        public ComputerBuilder builderCpu(String cpu) {
            this.cpu = cpu;
            return this;
        }

        public ComputerBuilder builderMainBoard(String mainBoard) {
            this.mainBoard = mainBoard;
            return this;
        }

        public ComputerBuilder builderHardDisk(String hardDisk) {
            this.hardDisk = hardDisk;
            return this;
        }

        public ComputerBuilder builderDisplayCard(String displayCard) {
            this.displayCard = displayCard;
            return this;
        }

        public ComputerBuilder builderPower(String power) {
            this.power = power;
            return this;
        }

        public ComputerBuilder builderMemory(String memory) {
            this.memory = memory;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Computer{" +
                "CPU='" + cpu + '\'' +
                ", mainBoard='" + mainBoard + '\'' +
                ", hardDisk='" + hardDisk + '\'' +
                ", displayCard='" + displayCard + '\'' +
                ", power='" + power + '\'' +
                ", memory='" + memory + '\'' +
                '}';
    }
}
