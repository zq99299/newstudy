package cn.mrcode.newstudy.design.pattern.principle.demeter;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 14:47
 */
public class Boss {
    public void commandCheckNumber(TeamLeader teamLeader) {
        // 对小组长下达检查指令
        teamLeader.checkNumber();
    }
}
