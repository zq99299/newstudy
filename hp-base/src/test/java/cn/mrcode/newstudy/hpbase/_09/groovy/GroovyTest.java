package cn.mrcode.newstudy.hpbase._09.groovy;

import groovy.lang.*;
import groovy.text.SimpleTemplateEngine;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * java调用groovy脚本
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/20 14:11
 */
public class GroovyTest {

    /**
     * 简单脚本执行
     */
    @Test
    public void evalScriptText() {
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        shell.setVariable("name", "zhuqiang");
        shell.evaluate("print(\"这是一个悲伤的故事作者：${name}\")");

        //在script中,声明变量,不能使用def,否则scrope不一致.
//        groovyShell.evaluate("def Date date = new Date();");
        shell.evaluate("date = new Date();");
        Date date = (Date) shell.getVariable("date");
        System.out.println(date);

        //以返回值的方式,获取script内部变量值,或者执行结果
        //一个shell实例中,所有变量值,将会在此"session"中传递下去."date"可以在此后的script中获取
        Long time = (Long) shell.evaluate("def time = date.getTime(); return time;");
        System.out.println("Time:" + time);

        binding.setVariable("list", new String[]{"A", "B", "C"});
        //invoke method
        String joinString = (String) shell.evaluate("def call(){return list.join(' - ')};call();");
        // 下面这样就不行
        //        shell.evaluate("def call(){return list.join(' - ')};");
//        String joinString = (String) shell.evaluate("call();");
        System.out.println("Array join:" + joinString);

    }

    /**
     * 运行完整脚本
     * @throws Exception
     */
    @Test
    public void evalScriptTextFull() throws Exception {
        StringBuffer buffer = new StringBuffer();
        //define API
        buffer.append("class User{")
                .append("String name;Integer age;")
                //.append("User(String name,Integer age){this.name = name;this.age = age};")
                .append("String sayHello(){return 'Hello,I am ' + name + ',age ' + age;}}\n");
        //Usage
        buffer.append("def user = new User(name:'zhangsan',age:1);")
                .append("user.sayHello();");
        //groovy.lang.Binding
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        String message = (String) shell.evaluate(buffer.toString());
        System.out.println(message);
        //重写main方法,默认执行
        String mainMethod = "static void main(String[] args){def user = new User(name:'lisi',age:12);print(user.sayHello());}";
        shell.evaluate(mainMethod);
        shell = null;
    }

    /**
     * 以面向"过程"的方式运行脚本
     */
    @Test
    public void evalScript() throws Exception {
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        //直接方法调用
        //shell.parse(new File(//))
        Script script = shell.parse("def join(String[] list) {return list.join('--');}");
        String joinString = (String) script.invokeMethod("join", new String[]{"A1", "B2", "C3"});
        System.out.println(joinString);
        ////脚本可以为任何格式,可以为main方法,也可以为普通方法
        //1) def call(){...};call();
        //2) call(){...};
        script = shell.parse("static void main(String[] args){i = i * 2;}");
        script.setProperty("i", new Integer(10));
        script.run();//运行,
        System.out.println(script.getProperty("i"));
        //the same as
        System.out.println(script.getBinding().getVariable("i"));
        script = null;
        shell = null;
    }

    /**
     * 解析groovy文件
     * @throws Exception
     */
    @Test
    public void parse() throws Exception {
        String rootDir = GroovyTest.class.getClassLoader().getResource("_09/groovy").getFile().substring(1);
        GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        File sourceFile = Paths.get(rootDir, "t1.groovy").toFile();
        Class testGroovyClass = classLoader.parseClass(new GroovyCodeSource(sourceFile));
        GroovyObject instance = (GroovyObject) testGroovyClass.newInstance();//proxy
        Long time = (Long) instance.invokeMethod("getTime", new Date());
        System.out.println(time);
        Date date = (Date) instance.invokeMethod("getDate", time);
        System.out.println(date.getTime());
    }

    // 如何加载已经编译的groovy文件(.class)
    @Test
    public void load() throws Exception {
        GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        String target = "G:\\xj\\dev\\project\\newstudy\\hp-base\\out\\test\\classes\\cn\\mrcode\\newstudy\\hpbase\\_09\\groovy\\TestGroovy.class";
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(target));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(bis, bos);
        Class testGroovyClass = classLoader.defineClass(null, bos.toByteArray());
        //instance of proxy-class
        //if interface API is in the classpath,you can do such as:
        //MyObject instance = (MyObject)testGroovyClass.newInstance()
        GroovyObject instance = (GroovyObject) testGroovyClass.newInstance();
        Long time = (Long) instance.invokeMethod("getTime", new Date());
        System.out.println(time);
        Date date = (Date) instance.invokeMethod("getDate", time);
        System.out.println(date.getTime());
        //here
        bis.close();
        bos.close();
        instance = null;
        testGroovyClass = null;
    }

    /**
     * 模版引擎
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @Test
    public void fun1() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        String rootDir = GroovyTest.class.getClassLoader().getResource("_09/webserver").getFile().substring(1);
        SimpleTemplateEngine simpleTemplateEngine = new SimpleTemplateEngine();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "x");
        Writable make = simpleTemplateEngine.createTemplate(Paths.get(rootDir, "html.tpl").toFile()).make(params);
        System.out.println(make.toString());
    }
}
