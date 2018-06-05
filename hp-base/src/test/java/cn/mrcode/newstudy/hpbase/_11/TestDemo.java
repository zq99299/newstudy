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
         *          1. 接受并解析客户端发送过来的命令；客户端有可能发送多个命令；需要解析
         *          2. 根据相应的命令执行操作；非特定命令就发送一大串字符给客户端
         *      客户端：
         *          1. 接受输入信息，并包装成命令发送给服务端
         *          2. 接受服务端的响应的数据
         *
         * 挑战2：
         *  改成多线程处理；并正确解决多线程下的粘包问题；服务端如何接受到命令，等上一个命令写完后再处理下一个命令还是要怎么做
         *  才能高效又不至于把所有数据都积压在服务端
         *</pre>
         *
         */
//        ByteBuffer.allocate(5)
    }
}

