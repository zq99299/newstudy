package cn.mrcode.newstudy.design.pattern.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程目录,根据业务场景选择的重写组件方法
 *
 * @author : zhuqiang
 * @date : 2018/12/26 21:52
 */
public class CourseCatalog extends CatalogComponent {
    private String name;
    private Integer level;
    private List<CatalogComponent> items = new ArrayList<>();

    public CourseCatalog(String name, Integer level) {
        this.name = name;
        this.level = level;
    }

    @Override
    public void add(CatalogComponent catalogComponent) {
        items.add(catalogComponent);
    }

    @Override
    public void remove(CatalogComponent catalogComponent) {
        items.remove(catalogComponent);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void print() {
        // 业务：课程目录打印的时候，需要打印该目录下的所有 item 的信息
        System.out.println(name);
        for (CatalogComponent item : items) {
            if (level != null) {
                for (int i = 0; i < level; i++) {
                    // 排版有层级
                    System.out.print("  ");
                }
            }
            item.print();
        }
    }
}
