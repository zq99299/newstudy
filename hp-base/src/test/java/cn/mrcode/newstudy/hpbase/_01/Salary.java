package cn.mrcode.newstudy.hpbase._01;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/1 21:48
 */
public class Salary {
    private String name;
    private int baseSalary;
    private int bonus;

    /** 获取年薪 */
    public int yearlySalary() {
        return baseSalary * 13 + bonus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(int baseSalary) {
        this.baseSalary = baseSalary;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}
