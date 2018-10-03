package cn.mrcode.newstudy.design.pattern.creational.singleton;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/10/3 15:42
 */
public enum EnumInstance {
    INSTANCE {
        @Override
        public void print() {
            System.out.println("INSTANCE");
        }
    };

    // 这样是全局的
    public abstract void print();

    private Object data;


    public static EnumInstance getInstance() {
        return INSTANCE;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
