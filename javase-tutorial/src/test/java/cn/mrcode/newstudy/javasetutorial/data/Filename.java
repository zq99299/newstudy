package cn.mrcode.newstudy.javasetutorial.data;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/09     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/9 16:39
 * @date 2017/12/9 16:39
 * @since 1.0.0
 */
public class Filename {
    private String fullPath; // 全路径
    private char pathSeparator,  // 路径分割符
            extensionSeparator; // 后缀

    public Filename(String str, char sep, char ext) {
        fullPath = str;
        pathSeparator = sep;
        extensionSeparator = ext;
    }

    public String extension() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        return fullPath.substring(dot + 1);
    }

    // gets filename without extension
    public String filename() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(sep + 1, dot);
    }

    public String path() {
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(0, sep);
    }
}
