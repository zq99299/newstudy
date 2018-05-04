package cn.mrcode.newstudy.hpbase._09.webserver;

import cn.mrcode.newstudy.hpbase._09.Method;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/4 21:37
 */
public class RequestWork implements Runnable {
    private Socket socket;
    private static int DEFAULT_BUFFER_SIZE = 8192;
    private byte[] HEADER_SEPARATOR = {'\r', '\n', '\r', '\n'};
    private InputStream input;
    private int head = 0; // buffer 中当前的位置
    private int tail = 0; // 每次读取到的有效字节数量
    private byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

    public RequestWork(Socket finalSocket) {
        this.socket = finalSocket;
        try {
            input = finalSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            Request request = preHeader();
            // 再根据解析出来的头 根据不同的type解析数据
            System.out.println(request);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(socket);
        }
    }

    private Request preHeader() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 找到正文处位置
        int i = 0;
        while (i < HEADER_SEPARATOR.length) {
            byte b = readByte();
            if (b == HEADER_SEPARATOR[i]) {
                i++;
            } else {
                i = 0;
            }
            baos.write(b);
        }
        Map<String, String> headers = new HashMap<>();

        Request request = null;
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                int lineNumber = reader.getLineNumber();
                if (lineNumber == 1) {
                    String[] els = line.split(" ");
                    String method = els[0].trim();
                    String uri = els[1].trim();
                    String version = els[2].trim();
                    int paramsIndex = uri.indexOf("?");
                    request = new Request();
                    request.setMethod(Method.lookup(method));
                    request.setHeaders(headers);
                    request.setUri(uri);
                    request.setProtocolVersion(version);
                    if (paramsIndex != -1) {
                        String masterUri = uri.substring(0, paramsIndex);
                        request.setUri(masterUri);
                        String paramsStr = uri.substring(paramsIndex + 1, uri.length());
                        StringTokenizer tokenizer = new StringTokenizer(paramsStr, "&");
                        Map<String, String> params = new HashMap<>();
                        while (tokenizer.hasMoreTokens()) {
                            String kvStr = tokenizer.nextToken();
                            String[] kvEle = kvStr.split("=");
                            params.put(kvEle[0].trim(), kvEle[1].trim());
                        }
                        request.setParams(params);
                    }
                    continue;
                }
                System.out.println(line);
                int kvSeparatorIndex = line.indexOf(58);
                if (kvSeparatorIndex == -1) {
                    continue;
                }
                String k = line.substring(0, kvSeparatorIndex).trim();
                String v = line.substring(kvSeparatorIndex + 1, line.length());
                headers.put(k, v);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.setCookies(parseCookies(headers.get("Cookie")));
        return request;
    }

    private Map<String, String> parseCookies(String cookieV) {
        if (cookieV != null && !cookieV.isEmpty()) {
            Map<String, String> cookies = new HashMap<>();
            StringTokenizer cookieToken = new StringTokenizer(cookieV, ";");
            while (cookieToken.hasMoreTokens()) {
                String[] cookEls = cookieToken.nextToken().split("=");
                if (cookEls.length != 2) {
                    continue;
                }
                cookies.put(cookEls[0], cookEls[1]);
            }
            return cookies;
        }
        return null;
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
