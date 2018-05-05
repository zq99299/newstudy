package cn.mrcode.newstudy.hpbase._09;

import cn.mrcode.newstudy.hpbase._09.coreserver.Method;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * socket模仿web服务器
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/29 22:39
 */
public class WebServer {
    public static String rootDir = WebServer.class.getClassLoader().getResource("_09").getFile().substring(1);
    public static Map<String, String> mimeTyps;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        AtomicInteger count = new AtomicInteger();
        try {
            Properties pro = new Properties();
            pro.load(Files.newInputStream(Paths.get(rootDir, "default-mimetypes.properties")));
            mimeTyps = (Map) pro;
            ServerSocket serverSocket = new ServerSocket(80);
            while (true) {
                Socket accept = serverSocket.accept();
                executor.execute(() -> {
                    try {
                        LineNumberReader in = new LineNumberReader(new InputStreamReader(accept.getInputStream()));
                        Request request = preHander(accept, in);
                        System.out.println(count.incrementAndGet() + " : " + request.getUri());
                        request.getHeaders().entrySet().stream()
                                .forEach(entry -> {
                                    System.out.println("\t" + entry);
                                });
                        doHandler(request, in, accept);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doHandler(Request request, LineNumberReader in, Socket accept) {
        try {
            String uri = request.getUri();
            if ("/".equals(uri)) {
                doRes(accept.getOutputStream(), Paths.get(rootDir, "index.html").toFile(), "text/html");
                return;
            }
            String mimeType = getMimeTypeForFile(uri);
            if ("text/html".equals(mimeType)) {
                if (Files.exists(Paths.get(rootDir, uri))) {
                    doRes(accept.getOutputStream(), Paths.get(rootDir, uri).toFile(), mimeType);
                    return;
                }
            } else {
                doRes(accept.getOutputStream(), Paths.get(rootDir, "404.html").toFile(), "text/html");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                accept.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getMimeTypeForFile(String uri) {
        int dot = uri.lastIndexOf('.');
        String mime = "";
        if (dot >= 0) {
            mime = mimeTyps.get(uri.substring(dot + 1).toLowerCase());
        }
        return mime;
    }

    /**
     * 先解析报文头，和基础参数
     * @param accept
     * @param in
     * @throws IOException
     */
    private static Request preHander(Socket accept, LineNumberReader in) throws IOException {
        String linInput;
        Map<String, String> headers = new HashMap<>();
        Request request = null;
        while ((linInput = in.readLine()) != null) {
            System.out.println(linInput);
            if (1 == in.getLineNumber()) { // 第一行为http协议的方法，请求路径，协议
                request = newRequest(linInput);
            } else {
                if (linInput.isEmpty()) {
                    in.mark(in.getLineNumber());
                    break;
                } else {
                    String[] els = linInput.split(":");
                    headers.put(els[0], els[1]);
                }
            }
        }
        if (request != null) {
            request.setHeaders(headers);
        }
        String cookie = headers.get("Cookie");
        if (cookie != null && !cookie.isEmpty()) {
            Map<String, String> cookies = new HashMap<>();
            StringTokenizer cookieToken = new StringTokenizer(cookie, ";");
            while (cookieToken.hasMoreTokens()) {
                String[] cookEls = cookieToken.nextToken().split("=");
                if (cookEls.length != 2) {
                    continue;
                }
                cookies.put(cookEls[0], cookEls[1]);
            }
            request.setCookies(cookies);
        }

        return request;
    }

    private static Request newRequest(String linInput) {
        StringTokenizer tokenizer = new StringTokenizer(linInput);
        if (!tokenizer.hasMoreTokens()) {
            throw new IllegalStateException("不支持的报文:" + linInput);
        }
        Method method = Method.lookup(tokenizer.nextToken());
        String uri = decodeUri(tokenizer.nextToken());
        String protocolVersion = tokenizer.nextToken();
        int paramsIndex = uri.indexOf("?");
        Map<String, String> params = null;
        if (paramsIndex != -1) {
            params = new HashMap<>();
            String paramsStr = uri.substring(paramsIndex + 1);
            uri = uri.substring(0, paramsIndex);
            StringTokenizer paramsToken = new StringTokenizer(paramsStr, "&");
            while (paramsToken.hasMoreTokens()) {
                String[] els = paramsToken.nextToken().split("=");
                if (els.length == 2) {
                    params.put(els[0], els[1]);
                }
            }
        }
        Request request = new Request(method, uri, protocolVersion);
        request.setParams(params);
        return request;
    }

    private static String decodeUri(String uri) {
        try {
            return URLDecoder.decode(uri, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    static final String CRLF = "\r\n";

    private static void doRequest(OutputStream outputStream) {
        URL resource = WebServer.class.getClassLoader().getResource("_09/index.html");
        File file = new File(resource.getFile());
//        Path path = Paths.get("hp-base/resources/_09/index.html");
//        File file = path.toFile();
        doRes(outputStream, file, "text/html");
    }

    private static void doRes(OutputStream outputStream, File file, String mimeType) {
        try (FileInputStream in = new FileInputStream(file);) {
            StringBuilder res = new StringBuilder();
            res.append("HTTP/1.1 200 OK\r\n")
                    .append("Date: " + new Date()).append(CRLF)
//                    .append("Content-Type: text/html;charset=utf-8").append(CRLF)
                    .append("Content-Type:").append(mimeType).append(CRLF)
                    .append("Content-Length:" + file.length()).append(CRLF)
                    .append(CRLF);
            outputStream.write(res.toString().getBytes("utf-8"));
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            outputStream.write(buffer);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
