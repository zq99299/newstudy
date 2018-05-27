package cn.mrcode.newstudy.hpbase._10.niostudy;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

/**
 * 文件锁定部分区域测试
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/26 20:46
 */
public class FileLockTest {
    int sizeofInt = 4;
    int indexStart = 0;
    int indexCount = 10;
    int indexSize = sizeofInt * indexCount;

    ByteBuffer buffer = ByteBuffer.allocate(indexSize);
    IntBuffer intBuffer = buffer.asIntBuffer();
    Random random = new Random();

    public static void main(String[] args) throws IOException, InterruptedException {
        Path file = Files.createTempFile("file", "lock");

        new Thread(() -> {
            try {
                handle(file, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                handle(file, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void handle(Path file, boolean writer) throws IOException, InterruptedException {

        RandomAccessFile raf = new RandomAccessFile(file.toFile(), writer ? "rw" : "r");
        FileChannel fc = raf.getChannel();
        FileLockTest fileLockTest = new FileLockTest();
        if (writer) {
            fileLockTest.doUpdates(fc);
        } else {
            fileLockTest.doQueries(fc);
        }
    }

    // 模拟一系列只读查询。
    // 在索引区域持有共享锁。
    private void doQueries(FileChannel fc) throws IOException, InterruptedException {
        while (true) {
            println("trying for shared lock...");
            // 获取共享锁只能是只读
            FileLock lock = fc.lock(indexStart, indexSize, true);
            int reps = random.nextInt(60) + 20;
            for (int i = 0; i < reps; i++) {
                int n = random.nextInt(indexCount);
                int position = indexStart + (n * sizeofInt);
                buffer.clear();
                fc.read(buffer, position);
                int value = intBuffer.get(n);
                println("index entry " + n + "=" + value);
                Thread.sleep(100);
            }
            lock.release();
            println("<sleeping>");
            Thread.sleep(random.nextInt(3000) + 500);
        }
    }

    private void doUpdates(FileChannel fc) throws IOException, InterruptedException {
        while (true) {
            println("trying for exclusive lock...");
            // 独占锁
            FileLock lock = fc.tryLock(indexStart, indexSize, false);
            if (lock == null) {
                continue;
            }
            updateIndex(fc);
            lock.release();
            println("<sleeping>");
            Thread.sleep(random.nextInt(2000) + 500);
        }
    }

    private int idxval = 1;

    private void updateIndex(FileChannel fc) throws InterruptedException, IOException {
        intBuffer.clear();
        for (int i = 0; i < indexCount; i++) {
            idxval++;
            println("Updating index " + i + "=" + idxval);
            intBuffer.put(idxval);
            Thread.sleep(500);
        }
        fc.write(buffer, indexStart);
        buffer.clear();
    }

    private int lastLineLen = 0;

    private void println(String msg) {
        System.out.print("\r\n ");
        System.out.print(msg);
        for (int i = msg.length(); i < lastLineLen; i++) {
            System.out.print(" ");
        }
        System.out.print("\r\n ");
        System.out.flush();
        lastLineLen = msg.length();
    }
}
