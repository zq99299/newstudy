package cn.mrcode.newstudy.design.pattern.structural.adapter.ac;

/**
 * 交流电-被适配者
 *
 * @author : zhuqiang
 * @date : 2018/12/25 21:25
 */
public class AC220 {
    public int outputAC220V() {
        int output = 220;
        System.out.println("交流电" + output + "V");
        return output;
    }
}
