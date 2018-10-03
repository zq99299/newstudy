package cn.mrcode.newstudy.design.pattern.creational.singleton;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/10/3 21:35
 */
public class ThreadLocalInstance {
    private ThreadLocalInstance() {
    }

    private static final ThreadLocal<ThreadLocalInstance> INSTANCE_THREAD_LOCAL
            = new ThreadLocal<ThreadLocalInstance>() {
        @Override
        protected ThreadLocalInstance initialValue() {
            return new ThreadLocalInstance();
        }
    };

    public static ThreadLocalInstance getInstance() {
        return INSTANCE_THREAD_LOCAL.get();
    }
}
