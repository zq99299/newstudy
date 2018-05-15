package cn.mrcode.newstudy.hpbase._09.webserver;

import cn.mrcode.newstudy.hpbase._09.coreserver.ContentType;
import cn.mrcode.newstudy.hpbase._09.coreserver.Request;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/14 23:35
 */
public class MultipartFormDataParse {

    public MultipartFormDataParse(Request request) {

    }

    public static boolean isMultipartFormData(Request request) {
        ContentType contentType = request.getContentType();
        String ct = contentType.getContentType();
        return ct.equalsIgnoreCase("multipart/form-data");
    }

    /**
     * 解析多部件数据
     * @return
     */
    public Map<String, MultipartItem> parse() {
        LinkedHashMap<String, MultipartItem> result = new LinkedHashMap<>();
        return result;
    }
}
