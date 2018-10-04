package cn.mrcode.newstudy.design.pattern.creational.prototype;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/10/4 22:55
 */
public class Mail implements Cloneable {
    private String name;
    private String email;
    private String content;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Mail() {
        // 后面方便查看克隆的时候是否调用了构造函数
        System.out.println("mail class constructor");
    }

    @Override
    public String toString() {
        return "Mail{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", content='" + content + '\'' +
                '}' + super.toString();  // 打印父类的 toString 方法，以便观察是否是同一个对象
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
