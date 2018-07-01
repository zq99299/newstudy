package cn.mrcode.newstudy.hpbase.mysql.mymysql2.protocol.text;

/**
 * 列
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/7/1 10:08
 */
public class Column {
    private String catalog;
    private String schema;
    private String table;
    private String orgTable;
    private String name;
    private String orgName;
    private long nextLength;
    private int characterSet;
    private long columnLength;
    private int columnType;
    private int flags;
    private int decimals;
    private int filler;

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        return schema;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public void setOrgTable(String orgTable) {
        this.orgTable = orgTable;
    }

    public String getOrgTable() {
        return orgTable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setNextLength(long nextLength) {
        this.nextLength = nextLength;
    }

    public long getNextLength() {
        return nextLength;
    }

    public void setCharacterSet(int characterSet) {
        this.characterSet = characterSet;
    }

    public int getCharacterSet() {
        return characterSet;
    }

    public void setColumnLength(long columnLength) {
        this.columnLength = columnLength;
    }

    public long getColumnLength() {
        return columnLength;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getFlags() {
        return flags;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setFiller(int filler) {
        this.filler = filler;
    }

    public int getFiller() {
        return filler;
    }
}
