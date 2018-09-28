package cn.mrcode.newstudy.design.pattern.creational.singleton;

/**
 * 双重检查
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/9/27 22:41
 */
public class LazyDoubleCheckSingleton {
    private static LazyDoubleCheckSingleton lazyDoubleCheckSingleton = null;

    private LazyDoubleCheckSingleton() {
    }

    public static LazyDoubleCheckSingleton getInstance() {
        // 第一层不加锁，有值则返回
        if (lazyDoubleCheckSingleton == null) {
            // 假设有2个线程在等待了
            synchronized (LazyDoubleCheckSingleton.class) {
                // 那么当第一个线程创建之后，其实是有值的了
                // 所以这里还需要判定一下，有值则不创建了
                // 所以这里是双重判定
                if (lazyDoubleCheckSingleton == null) {
                    lazyDoubleCheckSingleton = new LazyDoubleCheckSingleton();
                    // 这里有一个内存可见性的问题
                    // 上面这一句话其实分为三步
                    // 1. 分配内存给这个对象
                    // 2. 初始化对象
                    // 3. 设置 lazyDoubleCheckSingleton 指向刚分配的内存
                }
            }
        }
        return lazyDoubleCheckSingleton;
    }
}
