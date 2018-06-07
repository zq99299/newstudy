package cn.mrcode.newstudy.hpbase._11.mysinglethread;

import cn.mrcode.newstudy.hpbase._11.singlethread.LocalCmandUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 单线程版本：要记得是单线程；只不过把方法都分散到 各个对象里面了；
 * 相当于每个链接绑定了一个处理器
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/7 21:50
 */
public class IoHandler implements Runnable {
    private SelectionKey sk;
    private Selector selector;
    private ByteBuffer readerBuffer = ByteBuffer.allocate(100);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024 * 2);
    private SocketChannel sc;
    private int lastMsgPos;

    public IoHandler(Selector selector, SocketChannel accept) throws IOException {
        this.selector = selector;
        this.sc = accept;
        sk = accept.register(selector, 0);
        sk.attach(this);
        writeBuffer.put("Welcome to iohandler..\r\nTelnet>".getBytes());
        writeBuffer.flip();
        writeToChannel(); // 写入通道
    }

    private void writeToChannel() throws IOException {
        int writed = sc.write(writeBuffer);
        System.out.println("写入 " + writed);
        if (writeBuffer.hasRemaining()) {
            System.out.println("还未写完，剩余 " + writeBuffer.remaining());
        } else {
            System.out.println("已写完");
            writeBuffer.clear();
            // 如果已经写完则 取消写事件，恢复读事件
            sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
            sk.interestOps(sk.interestOps() | SelectionKey.OP_READ);
        }
    }

    @Override
    public void run() {
        try {
            if (sk.isReadable()) {
                doRead();
            } else if (sk.isWritable()) {
                writeToChannel();
            }
        } catch (IOException e) {
            e.printStackTrace();
            sk.cancel();
            IOUtils.closeQuietly(sc);
        }
    }

    private void doRead() throws IOException {
        // 当buffer满了。在读取，就放不进去了；就返回1
        // 但是 还是有可读的数据，所以会出现无限事件
        // 需要处理可用空间
        int read = sc.read(readerBuffer);
        System.out.println("读取 " + read);
        int readEnd = readerBuffer.position();
        String lineCommand = null;
        // 从上一次的位置开始读
        for (int i = lastMsgPos; i < readEnd; i++) {
            // 如果读取到了一行
            // telnet 是一个字符发送一次；只能保留输入的文字；会保留当前已经输入的文字；所以可以回车
            // 回车/r/n；
            if (readerBuffer.get(i) == 13) {
                byte[] lineByte = new byte[i - lastMsgPos];
                // 只能放在这个位置；否则就会丢掉数据
                // 这一段代码的原理其实就是： 废弃掉读的 ByteBuffer的limit
                // 自己来记录位置；业务读取操作是对buffer的写入；如果使用了clear那么之前的数据就不能保留了
                // 这次读取之后，要把位置重置到上一次读取的位置处
                readerBuffer.position(lastMsgPos);
                // 只会读取到有效的数据，而不会读取换行符
                // 下一次再读取的时候就从当前的换行符开始写入数据
                readerBuffer.get(lineByte);
                lineCommand = new String(lineByte);
                lastMsgPos = i;  // 记录上一次的位置
                break;
            }
        }
        if (lineCommand != null) {
            System.out.println("lineCommand: " + lineCommand);
            // 要处理命令所以取消读事件
            sk.interestOps(sk.interestOps() & ~SelectionKey.OP_READ);
            processCommand(lineCommand);
        }

        // 这里还有一个bug；如果一行数据超过50还没有换行符，将每次都会执行该方法，而又无效果
        if (readerBuffer.position() > readerBuffer.capacity() / 2) {
            System.out.println("清理已消耗数据,压缩buffer，腾出更多空间");
            // limit 为什么也要设置；是因为 compact copy的数据是 limit - position 为有效数据
            readerBuffer.limit(readerBuffer.position());
            // 从上一次位置之前的数据丢弃压缩
            readerBuffer.position(lastMsgPos);
            readerBuffer.compact();
            // 压缩之后上一次读取的位置变成0了。
            // 一定要重置为0 不然数据错误
            lastMsgPos = 0;
        }
    }

    private void processCommand(String lineCommand) throws IOException {
        // 如果是查询命令
        if (lineCommand.startsWith("dir")) {
            lineCommand = "cmd  /c " + lineCommand;
            String result = LocalCmandUtil.callCmdAndgetResult(lineCommand);
            // 控制台编码是GBK的
            writeBuffer.put(result.getBytes("GBK"));
            writeBuffer.put("\r\nTelnet>".getBytes());
        } else {
            // 否则生成固定大小的字符串写往客户端
            for (int i = 0; i < writeBuffer.capacity() - 9; i++) {
                writeBuffer.put((byte) ('a' + i % 25));
            }
            writeBuffer.put("\r\nTelnet>".getBytes());
        }
        writeBuffer.flip();
        writeToChannel();
    }
}
