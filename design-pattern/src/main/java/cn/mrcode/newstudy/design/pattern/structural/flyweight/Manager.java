package cn.mrcode.newstudy.design.pattern.structural.flyweight;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2018/12/25 22:59
 */
public class Manager implements Employee {
    /**
     * 部门固定
     */
    private String department;
    /**
     * 报告内容经常变更
     */
    private String reportContent;

    public Manager(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    @Override
    public void report() {
        System.out.println(reportContent);
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
