package cn.mrcode.newstudy.hpbase._04.java8;

/**
 * 菜单
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/1 16:01
 */
public class Dish {
    private final String name;
    private final boolean vegetarian; // 素
    private final int calories; // 卡路里含量
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }

    // 鱼类，肉类，其他
    public enum Type {
        MEAT, FISH, OTHER
    }
}
