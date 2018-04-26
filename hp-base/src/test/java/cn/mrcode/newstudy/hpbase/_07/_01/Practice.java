package cn.mrcode.newstudy.hpbase._07._01;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/25 21:32
 */
public class Practice {
    public void q() {
        // 问题：解释为什么下面放入会失败
        SynchronousQueue<String> queue = new SynchronousQueue();
        if (queue.offer("S1")) {
            System.out.println("scucess");
        } else {
            System.out.println("faield");
        }
    }

    @Test
    public void test1() {
        SynchronousQueue<String> queue = new SynchronousQueue();
        if (queue.offer("S1")) {
            System.out.println("scucess");
        } else {
            System.out.println("faield");
        }
    }

    @Test
    public void test2() {
        // 阻塞api，必须要等待一个接手的； put -> take
        SynchronousQueue<String> queue = new SynchronousQueue();

        List<Thread> ts = IntStream.range(0, 2)
                .mapToObj(i -> {
                    if (i == 0) {
                        return new Thread(() -> {
                            try {
                                queue.put("s1");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        return new Thread(() -> {
                            try {
                                String take = queue.take();
                                System.out.println(take);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                })
                .filter(t -> {
                    t.start();
                    return true;
                })
                .collect(Collectors.toList());
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void test3() {
        // offer 没人接手就直接返回；放入失败；poll 获取，没有人放入就直接返回
        // 该方式很难成功
        SynchronousQueue<String> queue = new SynchronousQueue();

        List<Thread> ts = IntStream.range(0, 2)
                .mapToObj(i -> {
                    if (i == 0) {
                        return new Thread(() -> {
                            while (queue.offer("s1")) {
                            }
                        });
                    } else {
                        return new Thread(() -> {
                            String poll = null;
                            while ((poll = queue.poll()) == null) {
                            }
                            System.out.println(poll);
                        });
                    }
                })
                .filter(t -> {
                    t.start();
                    return true;
                })
                .collect(Collectors.toList());
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void test4() {
        // 可以通过一头阻塞，一头不阻塞的方式来使用
        SynchronousQueue<String> queue = new SynchronousQueue();

        List<Thread> ts = IntStream.range(0, 2)
                .mapToObj(i -> {
                    if (i == 0) {
                        return new Thread(() -> {
                            try {
                                queue.put("s1");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        return new Thread(() -> {
                            String poll = null;
                            while (poll == null) {
                                poll = queue.poll();
                            }
                            System.out.println(poll);
                        });
                    }
                })
                .filter(t -> {
                    t.start();
                    return true;
                })
                .collect(Collectors.toList());
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
