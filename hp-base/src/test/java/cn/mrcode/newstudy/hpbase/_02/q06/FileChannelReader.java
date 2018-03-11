package cn.mrcode.newstudy.hpbase._02.q06;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * 用文件通道的形式实现按行读取
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/10 22:37
 */
public class FileChannelReader {

    private byte[] store; // 与buffer同样大小
    private int storeValidSize; // 有效字节数量
    private Charset sharset = Charset.forName("utf-8");
    private SeekableByteChannel channel;
    private ByteBuffer buffer;

    public FileChannelReader(Path filePath) throws IOException {
        channel = Files.newByteChannel(filePath);
        buffer = ByteBuffer.allocate(2048);
        store = new byte[2048];
    }

    public void readLine(Handler handler) throws IOException {
        while (channel.read(buffer) > 0) {
            int position = buffer.position();// 获取到当次读取到的有效内容长度
            byte[] array = buffer.array(); // 获取原始数组容器
            // decode

            // 查找换行符所在的位置
            // linux 换行符: \n
            // windows 换行符: \r\n
            // mac 换行符：\r
            int newLineIndex = -1;
            for (int i = 0; i < position; i++) {
                byte b = array[i];
                if (b == '\r' || b == '\n') {
                    // 当前读取到了换行符，并且有内容
                    if (storeValidSize != 0) {
                        handler.handler(new String(store, 0, storeValidSize, sharset));
                        Arrays.fill(this.store, (byte) 0);
                        this.storeValidSize = 0;
                    }
                    continue;
                }
                store[storeValidSize++] = b;
            }


            buffer.flip(); // 位置还原？
        }
    }

    public void close() throws IOException {
        channel.close();
    }

    public interface Handler {
        void handler(String line);
    }
}
