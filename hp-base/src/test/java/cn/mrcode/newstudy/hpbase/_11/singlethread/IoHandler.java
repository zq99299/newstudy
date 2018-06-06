package cn.mrcode.newstudy.hpbase._11.singlethread;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/6/6 22:22
 */
public class IoHandler implements Runnable {
    private Selector selector;
    private SocketChannel sc;
    private SelectionKey sk;
    private ByteBuffer writeBuffer;
    private ByteBuffer readBuffer;
    private int lastMessagePos;

    public IoHandler(Selector selector, SocketChannel sc) throws IOException {
        this.selector = selector;
        this.sc = sc;
        sk = sc.register(selector, SelectionKey.OP_READ);
        sk.attach(this);

        writeBuffer = ByteBuffer.allocateDirect(1024 * 2);
        readBuffer = ByteBuffer.allocateDirect(100);

        writeBuffer.put("Welcome Leader.us Power Man Java Course ...\r\nTelnet>".getBytes());
        writeBuffer.flip();
        doWriteData();
    }

    @Override
    public void run() {
        try {
            if (sk.isReadable()) {
                doReadData();
            } else if (sk.isWritable()) {
                doWriteData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sk.cancel();
            IOUtils.closeQuietly(sc);
        }
    }

    private void doWriteData() throws IOException {
        int writed = sc.write(writeBuffer);
        System.out.println("writed " + writed);
        if (writeBuffer.hasRemaining()) {
            System.out.println("writed " + writed + " not write finished  so bind to session ,remains " + writeBuffer.remaining());
            // 关注写事件
            sk.interestOps(sk.interestOps() | SelectionKey.OP_WRITE);
        } else {
            System.out.println(" block write finished ");
            writeBuffer.clear();
            // 写完之后再接受读事件
            sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
        }
    }

    private void doReadData() throws IOException {
        System.out.println("readed ");
        sc.read(readBuffer);
        int readEndPos = readBuffer.position();
        String readedLine = null;
        for (int i = lastMessagePos; i < readEndPos; i++) {
            //System.out.println(readBuffer.get(i));
            // 只判定一个换行符 是因为 telnet 是换行为发出数据；且只有一个字节；
            // 但是 还是需要自己写一个telnet 客户端。不然不能模拟发送命令
            if (readBuffer.get(i) == 13) {// a line finished
                byte[] lineBytes = new byte[i - lastMessagePos];
                readBuffer.position(lastMessagePos);
                readBuffer.get(lineBytes);
                lastMessagePos = i;
                readedLine = new String(lineBytes);
                System.out.println("received line ,lenth:" + readedLine.length() + " value " + readedLine);
                break;
            }
        }

        if (readedLine != null) {
            //取消读事件关注，因为要应答数据
            sk.interestOps(sk.interestOps() & ~SelectionKey.OP_READ);
            //处理指令
            processCommand(readedLine);

        }
        if (readBuffer.position() > readBuffer.capacity() / 2) {//清理前面读过的废弃空间
            System.out.println(" rewind read byte buffer ,get more space  " + readBuffer.position());
            readBuffer.limit(readBuffer.position());
            readBuffer.position(lastMessagePos);
            readBuffer.compact();
            lastMessagePos = 0;
        }
    }

    private void processCommand(String readedLine) throws IOException {

        if (readedLine.startsWith("dir")) {
            readedLine = "cmd  /c " + readedLine;
            String result = LocalCmandUtil.callCmdAndgetResult(readedLine);
            writeBuffer.put(result.getBytes("GBK"));
            writeBuffer.put("\r\nTelnet>".getBytes());
        } else {
            for (int i = 0; i < writeBuffer.capacity() - 10; i++) {
                writeBuffer.put((byte) ('a' + i % 25));
            }
            writeBuffer.put("\r\nTelnet>".getBytes());
        }
        writeBuffer.flip();
        doWriteData();
    }
}
