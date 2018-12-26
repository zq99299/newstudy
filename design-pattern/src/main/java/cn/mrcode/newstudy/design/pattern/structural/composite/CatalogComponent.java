package cn.mrcode.newstudy.design.pattern.structural.composite;

/**
 * 目录组件，把课程和目录都认为是目录组件
 *
 * @author : zhuqiang
 * @date : 2018/12/26 21:44
 */
public abstract class CatalogComponent {

    /**
     * 既然是目录，那么久有添加，移除等操作
     */
    public void add(CatalogComponent catalogComponent) {
        throw new UnsupportedOperationException("不支持添加操作");
    }

    public void remove(CatalogComponent catalogComponent) {
        throw new UnsupportedOperationException("不支持删除操作");
    }

    public String getName() {
        throw new UnsupportedOperationException("不支持获取名称操作");
    }

    public double getPrice() {
        throw new UnsupportedOperationException("不支持获取价格操作操作");
    }

    public void print() {
        throw new UnsupportedOperationException("不支持打印操作");
    }
}
