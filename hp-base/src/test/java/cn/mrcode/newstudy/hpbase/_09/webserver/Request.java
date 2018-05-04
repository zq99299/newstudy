package cn.mrcode.newstudy.hpbase._09.webserver;

import cn.mrcode.newstudy.hpbase._09.ContentType;
import cn.mrcode.newstudy.hpbase._09.Method;

import java.util.Map;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/4 22:14
 */
public class Request {
    private Method method;
    private String uri;
    private String protocolVersion;
    private Map<String, String> params;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private ContentType contentType;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
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
}
