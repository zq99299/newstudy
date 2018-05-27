package cn.mrcode.newstudy.hpbase._10.niostudy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Random;

/**
 * 管道的使用
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/27 16:07
 */
public class PipeTest {
    public static void main(String[] args) throws IOException {
        WritableByteChannel out = Channels.newChannel(System.out);
        ReadableByteChannel workerChannel = startWorker(10);
        ByteBuffer buffer = ByteBuffer.allocate(100);
        while (workerChannel.read(buffer) >= 0) {
            buffer.flip();
            out.write(buffer);
            buffer.clear();
        }
    }

    private static ReadableByteChannel startWorker(int reps) throws IOException {
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel sink = pipe.sink();
        // 只是一个线程写数据。一个线程读数据
        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            try {
                for (int r = 0; r < reps; r++) {
                    doSomeWork(buffer);
                    while (sink.write(buffer) > 0) {
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return pipe.source();
    }

    private static String[] products = {
            "No good deed goes unpunished",
            "To be, or what?",
            "No matter where you go, there you are",
            "Just say \"Yo\"",
            "My karma ran over my dogma"
    };
    private static Random rand = new Random();

    private static void doSomeWork(ByteBuffer buffer) {
        int product = rand.nextInt(products.length);
        buffer.clear();
        buffer.put(products[product].getBytes());
        buffer.put("\r\n".getBytes());
        buffer.flip();
    }
}
