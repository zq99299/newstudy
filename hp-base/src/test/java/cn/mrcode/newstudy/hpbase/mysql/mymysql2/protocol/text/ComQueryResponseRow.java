package cn.mrcode.newstudy.hpbase.mysql.mymysql2.protocol.text;

import java.util.List;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/1 15:06
 */
public class ComQueryResponseRow {
    private List<String> row;

    public ComQueryResponseRow(List<String> row) {
        this.row = row;
    }

    public List<String> getRow() {
        return row;
    }

    public void setRow(List<String> row) {
        this.row = row;
    }
}
