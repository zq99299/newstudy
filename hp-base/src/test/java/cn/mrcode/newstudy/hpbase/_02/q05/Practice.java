package cn.mrcode.newstudy.hpbase._02.q05;

import org.junit.Test;

/**
 * <pre>
 * 用装饰者模式实现如下的功能：
 * 要求用户输入一段文字，比如 Hello Me，然后屏幕输出几个选项
 *  1 ：加密
 *  2 ：反转字符串
 *  3：转成大写
 *  4：转成小写
 *  5：扩展或者剪裁到10个字符，不足部分用！补充
 *  6:用户输入 任意组合，比如 1，3 表示先执行1的逻辑，再执行3的逻辑
 *
 *  根据用户输入的选择，进行处理后，输出结果
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/10 21:38
 */
public class Practice {
    // 功能测试
    @Test
    public void main() {
        DefaultStringHandlerConcreteComponent component = new DefaultStringHandlerConcreteComponent();
        CncryptStringHandlerDecorator cncryptStringHandlerDecorator = new CncryptStringHandlerDecorator(component);
        ReverseStringHandlerDecorator reverseStringHandlerDecorator = new ReverseStringHandlerDecorator(component);
        UpperCaseStringHandlerDecorator upperCaseStringHandlerDecorator = new UpperCaseStringHandlerDecorator(component);
        LowerCaseStringHandlerDecorator lowerCaseStringHandlerDecorator = new LowerCaseStringHandlerDecorator(component);
        CutOrFillStringHandlerDecorator cutOrFillStringHandlerDecorator = new CutOrFillStringHandlerDecorator(component);
        String targetStr = "装饰器模仿测试He";
        System.out.println("被装饰处理字符串 : " + targetStr);
        System.out.println("===== 单个装饰器使用");
        System.out.println("加密 : " + cncryptStringHandlerDecorator.hander(targetStr));
        System.out.println("反转字符串 : " + reverseStringHandlerDecorator.hander(targetStr));
        System.out.println("转成大写 : " + upperCaseStringHandlerDecorator.hander(targetStr));
        System.out.println("转成小写 : " + lowerCaseStringHandlerDecorator.hander(targetStr));
        System.out.println("扩展或剪裁 : " + cutOrFillStringHandlerDecorator.hander(targetStr));
        System.out.println("===== 组合装饰器使用");

        System.out.println("加密、反转 : " + new ReverseStringHandlerDecorator(cncryptStringHandlerDecorator).hander(targetStr));
        System.out.println("转成大写、扩展或剪裁、反转字符串 : " +
                new ReverseStringHandlerDecorator(
                        new CutOrFillStringHandlerDecorator(new UpperCaseStringHandlerDecorator(component))
                ).hander(targetStr));
    }

    // 实现交互
    @Test
    public void main2() {
        String str = "123Hello中国";
        String rules = "1";
        printMenu();
        System.out.println("输入的字符串：" + str);
        System.out.println("组合规则是：" + rules);
        System.out.println();
        handler(str, rules);

        str = "123Hello中国";
        rules = "1 2";
        printMenu();
        System.out.println("输入的字符串：" + str);
        System.out.println("组合规则是：" + rules);
        handler(str, rules);
        System.out.println();

        str = "123Hello中国";
        rules = "1 2 4";
        printMenu();
        System.out.println("输入的字符串：" + str);
        System.out.println("组合规则是：" + rules);
        handler(str, rules);
        System.out.println();

        str = "123Hello中国";
        rules = "3 2 1";
        printMenu();
        System.out.println("输入的字符串：" + str);
        System.out.println("组合规则是：" + rules);
        handler(str, rules);
        System.out.println();

        str = "123Hello中国";
        rules = "4 3 2 1";
        printMenu();
        System.out.println("输入的字符串：" + str);
        System.out.println("组合规则是：" + rules);
        handler(str, rules);
    }

    public void printMenu() {
        System.out.println("======== 装饰器模式组合体验菜单 =======");
        System.out.println("1. 加密");
        System.out.println("2. 反转字符串");
        System.out.println("3. 转成大写");
        System.out.println("4. 转成小写");
        System.out.println("5. 扩展或剪裁");
    }

    public void handler(String str, String ruleStr) {
        DefaultStringHandlerConcreteComponent component = new DefaultStringHandlerConcreteComponent();
        String[] rules = ruleStr.split(" ");
        FilterStringHandlerDecorator decorator = null;
        for (String rule : rules) {
            switch (rule) {
                case "1":
                    if (decorator == null) {
                        decorator = new CncryptStringHandlerDecorator(component);
                    } else {
                        decorator = new CncryptStringHandlerDecorator(decorator);
                    }
                    break;
                case "2":
                    if (decorator == null) {
                        decorator = new ReverseStringHandlerDecorator(component);
                    } else {
                        decorator = new ReverseStringHandlerDecorator(decorator);
                    }
                    break;
                case "3":
                    if (decorator == null) {
                        decorator = new UpperCaseStringHandlerDecorator(component);
                    } else {
                        decorator = new UpperCaseStringHandlerDecorator(decorator);
                    }
                    break;
                case "4":
                    if (decorator == null) {
                        decorator = new LowerCaseStringHandlerDecorator(component);
                    } else {
                        decorator = new LowerCaseStringHandlerDecorator(decorator);
                    }
                    break;
                case "5":
                    if (decorator == null) {
                        decorator = new CutOrFillStringHandlerDecorator(component);
                    } else {
                        decorator = new CutOrFillStringHandlerDecorator(decorator);
                    }
                    break;
            }
        }
        String hander = decorator.hander(str);
        System.out.println("处理结果：" + hander);
    }
}
