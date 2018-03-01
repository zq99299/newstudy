package cn.mrcode.newstudy.hpbase._01.q05;

import java.util.Objects;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/3/1 22:30
 */
public class MyItem {
    private byte type;
    private byte color;
    private byte price;

    public MyItem(byte type, byte color, byte price) {
        this.type = type;
        this.color = color;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyItem myItem = (MyItem) o;
        return type == myItem.type &&
                color == myItem.color &&
                price == myItem.price;
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, color, price);
    }

    @Override
    public String toString() {
        return "MyItem{" +
                "type=" + type +
                ", color=" + color +
                ", price=" + price +
                '}';
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }

    public byte getPrice() {
        return price;
    }

    public void setPrice(byte price) {
        this.price = price;
    }
}
