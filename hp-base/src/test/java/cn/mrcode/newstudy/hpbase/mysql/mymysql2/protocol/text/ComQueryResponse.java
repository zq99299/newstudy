package cn.mrcode.newstudy.hpbase.mysql.mymysql2.protocol.text;

import cn.mrcode.newstudy.hpbase.mysql.mymysql2.ErrPacket;
import cn.mrcode.newstudy.hpbase.mysql.mymysql2.MySQLMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询命令响应
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/1 9:56
 */
public class ComQueryResponse {
    private int columnCount;
    private List<Column> columns;
    private List<ComQueryResponseRow> rows;
    private ErrPacket errPacket;
    private List<byte[]> mysqlOriginalPacket = new ArrayList<>();

    public ComQueryResponse(int columnCount) {
        this.columnCount = columnCount;
        columns = new ArrayList<>(columnCount);
        rows = new ArrayList<>();
    }

    public ComQueryResponse(ErrPacket errPacket) {
        this.errPacket = errPacket;
    }

    /**
     * @param data
     * @return 是否是eof包
     */
    public boolean parseAndAddColumn(byte[] data) {
        if (columnCount == columns.size()) {
            System.out.println("列定义已经是EOF包");
            return true;
        }
        MySQLMessage msm = new MySQLMessage(data);
        Column column = new Column();
        int patketLength = msm.readUB3();
        byte sqid = msm.read();
        System.out.println("patketLength " + patketLength + " ; sqid " + sqid);

        // lenenc_str
        // 目录（始终为“def”）
        column.setCatalog(msm.readStrWithLenenc());
        // 模式名称
        column.setSchema(msm.readStrWithLenenc());
        // 虚拟表名
        column.setTable(msm.readStrWithLenenc());
        // 物理表名
        column.setOrgTable(msm.readStrWithLenenc());
        //  虚拟列名称
        column.setName(msm.readStrWithLenenc());
        //  物理列名称
        column.setOrgName(msm.readStrWithLenenc());
        // 下一个字段长度（始终 0x0c）
        column.setNextLength(msm.readIntWithLenenc());
        // 列字符集 int.2
        column.setCharacterSet(msm.readUB2());
        // 列长度 int.4
        column.setColumnLength(msm.readUB4());
        // 列类型 int.1
        column.setColumnType(msm.readUB1());
        // int.2
        column.setFlags(msm.readUB2());
        // ? int.1
        column.setDecimals(msm.readUB1());
        // int.2 [00][00]
        column.setFiller(msm.readUB2());

        /** 所以这个是啥意思
         if command was COM_FIELD_LIST {
         lenenc_int     length of default-values
         string[$len]   default values
         }
         */
//        String s = msm.readStrWithLenenc();  // 读取报错
//        System.out.println(s);

        columns.add(column);
        return false;
    }

    public boolean parseAndAddRow(byte[] data) {
        if ((data[4] & 0xFF) == 0xFE) {
            return true;
        }
        MySQLMessage msm = new MySQLMessage(data);
        int patketLength = msm.readUB3();
        byte sqid = msm.read();
        System.out.println("patketLength " + patketLength + " ; sqid " + sqid);
        List<String> row = new ArrayList<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            row.add(msm.readStrWithLenenc());
        }
        ComQueryResponseRow responseRow = new ComQueryResponseRow(row);
        rows.add(responseRow);
        return false;
    }

    public ErrPacket getErrPacket() {
        return errPacket;
    }

    public void setErrPacket(ErrPacket errPacket) {
        this.errPacket = errPacket;
    }

    /**
     * 收集mysql的原始包，在本示例中，可以直接像前段连接发送该结果集
     * @param data
     */
    public void addMysqlOriginalPacket(byte[] data) {
        mysqlOriginalPacket.add(data);
    }

    public List<byte[]> getMysqlOriginalPacket() {
        return mysqlOriginalPacket;
    }
}
