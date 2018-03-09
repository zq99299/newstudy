package cn.mrcode.newstudy.hpbase._02.q04;

import cn.mrcode.newstudy.hpbase._01.Salary;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/9 21:13
 */
public class SalaryGroup {
    private String name;
    private Long yearlySalaryTotal = 0L;
    private List<Salary> salarys = new ArrayList<>(60);

    public SalaryGroup(String name) {
        this.name = name;
    }

    public void sum(int yearlySalary) {
        this.yearlySalaryTotal += yearlySalary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getYearlySalaryTotal() {
        return yearlySalaryTotal;
    }

    public void setYearlySalaryTotal(Long yearlySalaryTotal) {
        this.yearlySalaryTotal = yearlySalaryTotal;
    }

    public List<Salary> getSalarys() {
        return salarys;
    }

    public void setSalarys(List<Salary> salarys) {
        this.salarys = salarys;
    }
}
