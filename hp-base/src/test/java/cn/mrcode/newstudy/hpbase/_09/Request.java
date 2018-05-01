package cn.mrcode.newstudy.hpbase._09;

import java.io.BufferedInputStream;
import java.util.Map;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/30 16:18
 */
public class Request {
    private Method method;
    private String uri;
    private String protocolVersion;
    private Map<String, String> params;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private ContentType contentType;
    private BufferedInputStream inputStream;

    public Request(Method method, String uri, String protocolVersion) {
        this.method = method;
        this.uri = uri;
        this.protocolVersion = protocolVersion;
    }

    public Method getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
        if (headers.containsKey("Content-Type")) {
            this.contentType = ContentType.parse(headers.get("Content-Type"));
        }

    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setInputStream(BufferedInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public BufferedInputStream getInputStream() {
        return inputStream;
    }
}
