package cn.mrcode.newstudy.hpbase._09.coreserver;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 读取请求头如果采用了最底层的方式。那么body的读取将无法分离出去；所以该类作为一个备份记录，进行重构
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
    private RequestHandler handler;

    public RequestWork(Socket finalSocket, RequestHandler handler) {
        this.socket = finalSocket;
        this.handler = handler;
        try {
            // 解析头不能使用这原生流来实现；因为输入流不支持mark标记
            // 所以包装成了支持标记的流
            input = new BufferedInputStream(finalSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            Request request = preHeader();
            handler.handler(request);
            // 再根据解析出来的头 根据不同的type解析数据
            System.out.println(request);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(socket);
        }
    }

    private Request preHeader() {
        input.mark(DEFAULT_BUFFER_SIZE);
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
                System.out.println(line);
                int lineNumber = reader.getLineNumber();
                if (lineNumber == 1) {
                    String[] els = line.split(" ");
                    String method = els[0].trim();
                    String uri = els[1].trim();
                    String version = els[2].trim();
                    request = new Request();
                    request.setMethod(Method.lookup(method));
                    request.setUri(URLDecoder.decode(uri, "utf-8"));
                    request.setProtocolVersion(version);
                    continue;
                }

                int kvSeparatorIndex = line.indexOf(58);
                if (kvSeparatorIndex == -1) {
                    continue;
                }
                String k = line.substring(0, kvSeparatorIndex).trim();
                String v = line.substring(kvSeparatorIndex + 1, line.length()).trim();
                headers.put(k, v);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> cookies = parseCookies(headers.get("Cookie"));
        if (cookies != null) {
            request.getCookies().putAll(cookies);
        }
        request.setContentType(ContentType.parse(headers.get("Content-Type")));
        request.getHeaders().putAll(headers);
        try {
            input.reset();
            // 跳过头+空行
            input.skip(baos.size());
            request.setInput(input);
            request.setOutput(socket.getOutputStream());
            parseParams(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }

    private void parseParams(Request request) {
        String uri = request.getUri();
        int paramsIndex = uri.indexOf("?");
        if (paramsIndex != -1) {
            String masterUri = uri.substring(0, paramsIndex);
            request.setUri(masterUri);
            String paramsStr = uri.substring(paramsIndex + 1, uri.length());
            request.getParams().putAll(getUriParams(paramsStr));
        }

        if (request.getMethod() == Method.POST
                && request.getContentType().getContentType().equalsIgnoreCase("application/x-www-form-urlencoded")) {
            // 读取流然后解析参数
            BufferedInputStream bis = (BufferedInputStream) request.getInput();
            byte[] buf = new byte[Integer.parseInt(request.getHeaders().get("Content-Length"))];
            try {
                bis.read(buf);
                String paramsStr = new String(buf, 0, buf.length, "utf-8");
                if (paramsStr != null && !paramsStr.isEmpty()) {
                    paramsStr = URLDecoder.decode(paramsStr, "utf-8");
                    request.getParams().putAll(getUriParams(paramsStr));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> getUriParams(String paramsStr) {
        StringTokenizer tokenizer = new StringTokenizer(paramsStr, "&");
        Map<String, String> params = new HashMap<>();
        while (tokenizer.hasMoreTokens()) {
            String kvStr = tokenizer.nextToken();
            String[] kvEle = kvStr.split("=");
            String k = kvEle[0];
            String v = "";
            if (kvEle.length == 2) {
                v = kvEle[1];
            }
            params.put(k, v);
        }
        return params;
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
