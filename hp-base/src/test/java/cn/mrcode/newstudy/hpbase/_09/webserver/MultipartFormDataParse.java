package cn.mrcode.newstudy.hpbase._09.webserver;

import cn.mrcode.newstudy.hpbase._09.coreserver.ContentType;
import cn.mrcode.newstudy.hpbase._09.coreserver.Request;

import java.io.*;
import java.util.*;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/14 23:35
 */
public class MultipartFormDataParse {
    private static int DEFAULT_BUFFER_SIZE = 8192;
    private byte[] HEADER_SEPARATOR = {'\r', '\n', '\r', '\n'};
    private InputStream input;
    private int head = 0; // buffer 中当前的位置
    private int tail = 0; // 每次读取到的有效字节数量
    private String boundary; // 分割标识符
    private String itemBoundaryPrefix = "--"; // item分割符前缀
    private String itemBoundary; // item 开始分割符
    private String itemBoundaryBodyEnd; // item结束分割符
    private byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    private Request request;

    public MultipartFormDataParse(Request request) {
        this.request = request;
        input = request.getInput();

        String boundary = request.getContentType().getBoundary();
        itemBoundary = itemBoundaryPrefix + boundary;
        itemBoundaryBodyEnd = "\r\n" + itemBoundary;
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

        ByteArrayOutputStream headerBaos = findHeader();
        Map<String, String> multipartHeaders = getMultipartHeaders(headerBaos);
        Map<String, String> subHeaders = getSubHeaders(multipartHeaders);

        String name = subHeaders.get("name");
        String filename = subHeaders.get("filename");
        // 如果filename有值，那么一定是一个文件
        // 否则是一个普通的kv item
        if (filename != null) {

        } else {
            // 找到item的结尾处。其实也是把body的内容找出来
            ByteArrayOutputStream itemBody = new ByteArrayOutputStream();
            byte[] ibbe = itemBoundaryBodyEnd.getBytes();
            int i = 0;
            ArrayList<Byte> tempBody = new ArrayList<>(ibbe.length);
            while (i < ibbe.length) {
                byte b = readByte();
                if (b == ibbe[i]) {
                    tempBody.add(b);
                    i++;
                } else {
                    // 不是分隔符
                    // 清空临时存储区，重置计数器
                    i = 0;
                    if (tempBody.isEmpty()) {
                        itemBody.write(b);
                    } else {
                        for (Byte aByte : tempBody) {
                            itemBody.write(aByte);
                        }
                        tempBody.clear();
                    }
                }
            }
            MultipartItemKV kv = new MultipartItemKV(name);
            kv.setValue(itemBody.toString());
            result.put(name, kv);
        }

        return result;
    }

    /**
     * 找到body开始处；并返回分割符和item的请求头
     * @return
     */
    private ByteArrayOutputStream findHeader() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = 0; // 记录匹配的连续字符数量
        while (i < HEADER_SEPARATOR.length) {
            byte b = readByte();
            if (b == HEADER_SEPARATOR[i]) {
                i++;
            } else {
                i = 0;
            }
            baos.write(b);
        }
        return baos;
    }

    /**
     * ------WebKitFormBoundaryn5Ad5zhsm1GIz2or\r\n
     * Content-Disposition: form-data; name="f1"; filename="power d链接数据库.txt"\r\n
     * Content-Type: text/plain\r\n\r\n
     * 获取分隔符下；一个item的请求头；按行分割
     * @param baos
     * @return
     */
    private Map<String, String> getMultipartHeaders(ByteArrayOutputStream baos) {
        LineNumberReader headerLnr = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
        Map<String, String> multipartHeaders = new HashMap<>();
        headerLnr.lines()
                .forEach(lin -> {
                    if (lin.trim().equals(itemBoundary)) {
                        return;
                    }
                    int i1 = lin.indexOf(":");
                    if (i1 == -1) {
                        return;
                    }
                    String key = lin.substring(0, i1).trim();
                    String value = lin.substring(i1 + 1).trim();
                    multipartHeaders.put(key, value);
                });
        return multipartHeaders;
    }

    /**
     * 获取 Content-Disposition 中包含的键值对
     * @param multipartHeaders
     * @return
     */
    private Map<String, String> getSubHeaders(Map<String, String> multipartHeaders) {
        Map<String, String> subHeaders = new HashMap<>();
        if (multipartHeaders.containsKey("Content-Disposition")) {
            String cd = multipartHeaders.get("Content-Disposition");
            StringTokenizer st = new StringTokenizer(cd, ";");
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                int i1 = s.indexOf("=");
                if (i1 == -1) {
                    subHeaders.put(s, null);
                } else {
                    String trim = s.substring(i1 + 1).trim();
                    subHeaders.put(s.substring(0, i1).trim(), trim.substring(1, trim.length() - 1));
                }
            }
        }
        return subHeaders;
    }

    private byte readByte() {
        // buffer 已消耗光了
        if (head == tail) {
            try {
                int read = input.read(buffer);
                if (read == -1) {
                    throw new RuntimeException("bad request");
                }
                head = 0;
                tail = read;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer[head++];
    }
}
