package cn.mrcode.newstudy.design.pattern.structural.adapter.ac;

/**
 * 充电器适配器
 *
 * @author : zhuqiang
 * @date : 2018/12/25 21:27
 */
public class PowerAdapter implements DC5 {
    private AC220 ac220 = new AC220();

    @Override
    public int outputDC5V() {
        int adapterIntput = ac220.outputAC220V();
        // 假设这个是变压器工作逻辑
        int adapterOutput = adapterIntput / 44;
        System.out.println("PowerAdapter 输出直流电" + adapterOutput + "V");
        return adapterOutput;
    }
}
