package cn.mrcode.newstudy.hpbase._09.coreserver;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/1 14:55
 */
public class ContentType {
    private String boundary;
    private String contentType;

    public ContentType(String contentType, String boundary) {
        this.boundary = boundary;
        this.contentType = contentType;
    }

    public static ContentType parse(String contentTypeheader) {
        if (contentTypeheader == null) {
            return null;
        }
        if (contentTypeheader.indexOf(";") != -1) {
            String[] els = contentTypeheader.split(";");
            String ct = els[0].trim();
            String boundary = null;
            if ("multipart/form-data".equals(ct)) {
                boundary = els[1].split("=")[1];
            }
            return new ContentType(ct, boundary);
        } else {
            return new ContentType(contentTypeheader, null);
        }
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
