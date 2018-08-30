package cn.mrcode.newstudy.design.pattern.creational.builder;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/30 22:27
 */
// 实际的建造者，可以认为是 员工
public class ActualBuilder extends Builder {
    private Computer computer = new Computer();

    @Override
    public Computer createComputer() {
        return computer;
    }

    @Override
    public void buildCPU(String cpu) {
        computer.setCPU(cpu);
    }

    @Override
    public void buildMainBoard(String mainBoard) {
        computer.setMainBoard(mainBoard);
    }

    @Override
    public void buildHardDisk(String hardDisk) {
        computer.setHardDisk(hardDisk);
    }

    @Override
    public void buildDisplayCard(String displayCard) {
        computer.setDisplayCard(displayCard);
    }

    @Override
    public void buildPower(String power) {
        computer.setPower(power);
    }

    @Override
    public void buildMemory(String memory) {
        computer.setMemory(memory);
    }
}
