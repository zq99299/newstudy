package cn.mrcode.newstudy.design.pattern.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元工厂
 *
 * @author : zhuqiang
 * @date : 2018/12/25 23:02
 */
public class EmployeeFactory {
    private final static Map<String, Employee> EMPLOYEE_MAP = new HashMap<>();

    public Employee getManger(String department) {
        Manager manager = (Manager) EMPLOYEE_MAP.get(department);
        if (manager == null) {
            manager = new Manager("department");
            System.out.print("创建部门经理 " + department);
            String reportContent = " 部门汇报：内容是...";
            manager.setReportContent(reportContent);
            System.out.println(" 创建报告 " + reportContent);
            EMPLOYEE_MAP.put(department, manager);
        }
        return manager;
    }
}
