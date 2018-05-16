package cn.mrcode.newstudy.hpbase._09.webserver;

import java.nio.file.Path;

/**
 * 文件对象
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/14 23:46
 */
public class MultipartItemFile extends MultipartItem {
    private String filename;
    private Path path;

    public MultipartItemFile(String name) {
        super(name);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
