package cn.mrcode.newstudy.hpbase._12.niorector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/12 22:39
 */
public class TelnetIOHandler extends IOHandler {
    private int lastMessagePos;

    public TelnetIOHandler(Selector selector, SocketChannel sc) throws IOException {
        super(selector, sc);
    }

    @Override
    protected void onConnected() throws IOException {
        System.out.println("connected from " + this.socketChannel.getRemoteAddress());
        this.writeData("Welcome Leader.us Power Man Java Course ...\r\nTelnet>".getBytes());
    }

    @Override
    protected void doHandler() throws IOException {
        socketChannel.read(readBuffer);
        int readEndPos = readBuffer.position();
        String readedLine = null;
        for (int i = lastMessagePos; i < readEndPos; i++) {
            if (readBuffer.get(i) == 13) {
                byte[] lineBytes = new byte[i - lastMessagePos];
                readBuffer.position(lastMessagePos);
                readBuffer.get(lineBytes);
                lastMessagePos = i;
                readedLine = new String(lineBytes);
                System.out.println("received line ,length:" + readedLine.length() + " value " + readedLine);
                break;
            }
        }

        // 处理命令
        if (readedLine != null) {
            processCommond(readedLine);
        } else {
            selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_READ);
        }

        // 清理读缓存
        // 已使用一半的空间，则压缩空间，丢弃掉已读数据
        // 但是这里有一个bug；当一行命令超过51的时候每次都会调用该方法
        if (readBuffer.position() > readBuffer.capacity() / 2) {
            readBuffer.limit(readBuffer.position());
            readBuffer.position(lastMessagePos);
            readBuffer.compact();
            lastMessagePos = 0;
        }
    }

    private void processCommond(String readedLine) throws IOException {
        if (readedLine.isEmpty()) {
            this.writeData("\r\nTelnet>".getBytes());
            return;
        }
        ByteBuffer buf = ByteBuffer.allocate(2048);
        // 模拟写入一些字符串
        for (int i = 0; i < buf.capacity() - 9; i++) {
            // a = 97 ; z = 122 下面这个25 = 122 -97
            buf.put((byte) (i % 25 + 'a'));
        }
        buf.put("\r\nTelnet>".getBytes());
        buf.flip();
        this.writeData(buf.array());
    }
}
