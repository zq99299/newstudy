package cn.mrcode.newstudy.hpbase._10.niostudy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 多线程来处理已就绪通道;
 * <p>
 * 模拟了一个work线程池；和 work线程；
 * <p>
 * 对于通道的数据key事件； 客户端发送n多数据，那么在下一次你处理的时候这些数据将会被拿到；
 * 可用暂时改变他的状态；这个流程逻辑还不是理解的很清楚；要想搞明白 为什么修改兴趣之后，有一个时间差？生效？要搞懂这个选择过程是怎么的。就明白了
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/27 23:17
 */
public class SelectSocketsThreadPool extends SelectSockets {
    private static final int MAX_THREADS = 5;
    private ThreadPool pool = new ThreadPool(MAX_THREADS);

    public static void main(String[] args) throws IOException {
        args = new String[]{"9857"};
        new SelectSocketsThreadPool().go(args);
    }

    @Override
    protected void readDataFromSocket(SelectionKey key) throws IOException {
        WorkThread work = pool.getWork();
        if (work == null) {
            // 没有可用线程则不处理，直接返回
            return;
        }
        work.serviceChannel(key);
    }

    private class ThreadPool {
        // 闲置的线程池
        private List<WorkThread> idle = new LinkedList<>();

        public ThreadPool(int maxThreads) {
            for (int i = 0; i < maxThreads; i++) {
                WorkThread workThread = new WorkThread(this);
                workThread.setName("Worker" + (i + 1));
                workThread.start();
                idle.add(workThread);
            }
        }

        WorkThread getWork() {
            WorkThread workThread = null;
            synchronized (idle) {
                if (idle.size() > 0) {
                    workThread = idle.remove(0);
                }
            }
            return workThread;
        }

        public void returnWorker(WorkThread workThread) {
            synchronized (idle) {
                idle.add(workThread);
            }
        }

//        List<>
    }

    private class WorkThread extends Thread {
        private ByteBuffer buffer = ByteBuffer.allocate(1024);
        private SelectSocketsThreadPool.ThreadPool pool;
        private SelectionKey key;

        WorkThread(ThreadPool pool) {
            this.pool = pool;
        }

        @Override
        public synchronized void run() {
            System.out.println(this.getName() + " is ready");
            while (true) {
                try {
                    // 睡眠并释放锁对象
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 清除中断状态
                    this.isInterrupted();
                }
                if (key == null) {
                    continue;
                }

                System.out.println(this.getName() + " 已经被唤醒");
                try {
                    drainChannel(key);
                } catch (Exception e) {
                    System.out.println("Caught（扑捉）‘" + e + "' closing channel");
                    try {
                        key.channel().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    // 让选择器在阻塞状态中立即恢复
                    key.selector().wakeup();
                }
                key = null;
                this.pool.returnWorker(this);
            }
        }

        synchronized void serviceChannel(SelectionKey key) {
            this.key = key;
            // 更新兴趣，忽略对该key的读的准备
            key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
            this.notify();
        }

        private void drainChannel(SelectionKey key) throws IOException {
            SocketChannel channel = (SocketChannel) key.channel();
            int count;
            buffer.clear(); // Empty buffer
            while ((count = channel.read(buffer)) > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                buffer.clear();
            }
            if (count < 0) {
                channel.close();
                return;
            }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 更改他的读
            // Resume interest in OP_READ
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
            // Cycle the selector so this key is active again
            key.selector().wakeup();
        }
    }
}
