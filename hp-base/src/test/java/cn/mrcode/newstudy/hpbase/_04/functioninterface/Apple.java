package cn.mrcode.newstudy.hpbase._04.functioninterface;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/19 22:43
 */
public class Apple {
    private String color;
    private float weight;

    public Apple(String color, float weight) {
        this.color = color;
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "color='" + color + '\'' +
                ", weight=" + weight +
                '}';
    }
}