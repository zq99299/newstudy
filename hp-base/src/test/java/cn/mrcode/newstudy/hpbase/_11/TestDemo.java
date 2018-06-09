package cn.mrcode.newstudy.hpbase._11;

import org.junit.Test;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/5 22:12
 */
public class TestDemo {
    @Test
    public void fun1() {
        /**
         * <pre>
         * 挑战1：
         * 使用 经典版的reactor模型实现TelnetServer;
         * reactor模型组成：
         *  acceptor 专门用来处理接受链接和初始化配置链接
         *  ioHandler 专门用来处理读写事件；
         *
         * 是一个单线程的处理版本；
         * 要实现的功能有：
         *      服务端：
         *          1. 接受并解析客户端发送过来的命令；客户端有可能发送多个命令；需要解析；
         *              要注意的是：接收一个命令就暂时取消读事件，等待写完数据后，再恢复读事件
         *          2. 根据相应的命令执行操作；非特定命令就发送一大串字符给客户端；
         *              数据一次未写完则下次继续；写完则恢复读事件，取消写事件
         *      客户端Tennet：
         *          1. 输入一个发送一个字符，以回车结尾；可以输入多个字符然后主动回车
         *          2. 打印服务端的数据
         *
         * 挑战2：
         *</pre>
         *
         */
//        ByteBuffer.allocate(5)
    }

}

