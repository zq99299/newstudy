package cn.mrcode.newstudy.hpbase._12.myniorector;

import cn.mrcode.newstudy.hpbase._07._02.Practice;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/15     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/15 13:52
 * @date 2018/6/15 13:52
 * @since 1.0.0
 */
public class TelnetHandler extends MyIOHandler {
    private ByteBuffer readBuffer = ByteBuffer.allocate(100);
    private int lastPos;

    public TelnetHandler(SelectionKey sk) throws IOException {
        super(sk);
    }

    @Override
    protected void onConnected() throws IOException {
        sendMenu();
    }

    @Override
    protected void doHandler() throws IOException {
        int read = sc.read(readBuffer);
        System.out.println("当次读到数据 " + read);
        // 获取到当次独到的有效数据，手动模拟filp，记录limit
        int readEnd = readBuffer.position();
        // 从上一次未读完的开始读,不能在这里进行重置，因为上一次留下的是非换行符
//        readBuffer.position(lastPos);
        String readedLine = null;
        // 找到换行符
        for (int i = lastPos; i < readEnd; i++) {
            // 找到换行符
            if (readBuffer.get(i) != 13) {
                continue;
            }

            // 获取一次有效的命令数据
            byte[] bytes = new byte[i - lastPos];
            readBuffer.position(lastPos);
            readBuffer.get(bytes);
            readedLine = new String(bytes);
            lastPos = i;
        }

        if (readedLine != null) {
            processCommond(readedLine);
        } else {
            sk.interestOps(sk.interestOps() | SelectionKey.OP_READ);
        }


        if (readBuffer.position() > readBuffer.capacity() / 2) {
            readBuffer.limit(readBuffer.position());
            readBuffer.position(lastPos);
            readBuffer.compact();
            lastPos = 0;
        }
        sk.selector().wakeup();
    }


    private void processCommond(String readedLine) throws IOException {
        if ("2".equalsIgnoreCase(readedLine)) {
            this.write("Bye My Power boy ..".getBytes());
            sc.close();
        } else if (readedLine.startsWith("1")) {
            doFindKeywordInFiles(readedLine);
        } else {
            sendMenu();
        }
    }

    private void sendMenu() throws IOException {
        this.write(("1: find keyword in files\r\n2: quit" +
                "\r\nTelnet>").getBytes());
    }

    /**
     * 接收命令：从指定目录下查找指定最大深度的文件，统计key出现了多少次；文件分别是什么
     * @param readedLine
     */
    private void doFindKeywordInFiles(String readedLine) throws IOException {
        String[] params = readedLine.split(" ");
        String dir = null;
        String keyword = null;
        int maxDepth = 8;
        if (params.length < 3) {
            this.write("example : 1 d:/mydata java [maxDepth - Maximum depth is optional]".getBytes());
            sk.interestOps(sk.interestOps() | SelectionKey.OP_READ);
            sk.selector().wakeup();
            return;
        }
        if (params.length == 3) {
            dir = params[1];
            keyword = params[2];
        }

        if (params.length > 3) {
            maxDepth = Integer.parseInt(params[3]);
        }

        // 开始统计
        Practice.SearchResult searchResult = new Practice().searchInFiles(keyword, dir, maxDepth);
        this.write(JSON.toJSONString(searchResult).getBytes());
    }
}
