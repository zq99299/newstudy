package cn.mrcode.newstudy.design.pattern.creational.prototype;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ${todo}
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/10/4 22:56
 */
public class MailTest {
    @Test
    public void sendMails() {
        for (int i = 0; i < 5; i++) {
            Mail mail = new Mail();
            mail.setName("姓名" + i);
            mail.setEmail(i + "@mrcode.cn");
            // 内容一样：模拟原型场景中非常耗时的几个操作，该操作是共享的
            mail.setContent("恭喜你中奖了");
            send(mail);
        }
    }

    public void send(Mail mail) {
        // 该方法模拟邮件发送操作
        System.out.println("邮件发送：" + mail);
    }

    @Test
    public void sendMailsForClone() throws CloneNotSupportedException {
        Mail mail = new Mail();
        mail.setContent("恭喜你中奖了");
        for (int i = 0; i < 5; i++) {
            Mail mailTempl = (Mail) mail.clone();
            mailTempl.setName("姓名" + i);
            mailTempl.setEmail(i + "@mrcode.cn");
            send(mailTempl);
        }
    }

}