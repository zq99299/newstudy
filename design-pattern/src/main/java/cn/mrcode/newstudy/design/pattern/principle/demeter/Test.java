package cn.mrcode.newstudy.design.pattern.principle.demeter;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/26 14:51
 */
public class Test {
    public static void main(String[] args) {
        Boss boss = new Boss();
        TeamLeader teamLeader = new TeamLeader();
        boss.commandCheckNumber(teamLeader);
    }
}
