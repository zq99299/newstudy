package cn.mrcode.newstudy.hpbase._09;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

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
public class WebServer2 {
    public static String rootDir = WebServer2.class.getClassLoader().getResource("_09").getFile().substring(1);
    public static Map<String, String> mimeTyps;
    // bufferdIs默认8192字节，8k;每次取出8k
    public static int DEFAULT_BUFFER_SIZE = 8192;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        AtomicInteger count = new AtomicInteger();
        try {
            Properties pro = new Properties();
            pro.load(Files.newInputStream(Paths.get(rootDir, "default-mimetypes.properties")));
            mimeTyps = (Map) pro;
            ServerSocket serverSocket = new ServerSocket(6360);
            while (true) {
                Socket accept = serverSocket.accept();
                executor.execute(() -> {
                    try {
                        preRequest(accept);
                        accept.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void preRequest(Socket accept) {
        try {

            // 不支持 mark/reset操作，所以需要包装成支持的流
            BufferedInputStream bis = new BufferedInputStream(accept.getInputStream());

            int headerLength = getHeaderLength(bis, DEFAULT_BUFFER_SIZE);
            byte[] headrEls = new byte[headerLength];
            bis.read(headrEls);
            LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(headrEls)));
            Request request = preHander(accept, lineNumberReader);
            request.setInputStream(bis);
            bis.mark(1);
            if (request.getMethod() == Method.POST && request.getContentType().getContentType().equalsIgnoreCase("multipart/form-data")) {
                // 解析参数；这个从流中 需要定位分隔符和二进制数据
                // 简直太难了
                parseMultipart(request);
            }
            bis.reset();
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
            reader.lines().forEach(System.out::println);
            // 找到请求头结尾的地方
//            IOUtils.closeQuietly(bis);  // 工具类提供的安全关闭流的方法
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

    }

    private static void parseMultipart(Request request) throws IOException, FileUploadException {
        NanoFileUpload uploader = new NanoFileUpload(new DiskFileItemFactory());
        Map<String, List<FileItem>> stringListMap = uploader.parseParameterMap(request);
        System.out.println(stringListMap);
        BufferedInputStream bis = request.getInputStream();
        bis.mark(DEFAULT_BUFFER_SIZE);
        byte[] bufer = new byte[DEFAULT_BUFFER_SIZE];
        int read = bis.read(bufer);
        bis.reset();

    }

    private static int getHeaderLength(BufferedInputStream bis, int DEFAULT_BUFFER_SIZE) throws IOException {
        bis.mark(DEFAULT_BUFFER_SIZE);
        byte[] bufer = new byte[DEFAULT_BUFFER_SIZE];
        int read = bis.read(bufer);
        if (read == -1) {
            throw new RuntimeException("错误的请求");
        }
        int headerEnd = 0;
        int currentLength = 0;
        while (read > 0) {
            headerEnd = findHeaderEnd(bufer, read);
            currentLength += read;
            if (headerEnd > 0) {
                // 标识在该数据中找到了请求头的尾部位置
                break;
            }
            if (read == bufer.length) {
                // 如果当次读取到的字符与buffer相等，那么需要清空该bufer，再次读取
                // 偏移4是因为害怕上一次读取到数据遗留了一部分，4byte = "\r\n"
                System.arraycopy(bufer, 0, bufer, read - 4, 4);
                currentLength -= 4;
                read = bis.read(bufer, 4, DEFAULT_BUFFER_SIZE - 4);
            } else {
                // 如果当前的buffer没有读满，则需要续读
                read = bis.read(bufer, read, DEFAULT_BUFFER_SIZE - read);
            }
        }
        // 请求头长度
        int headerLength = currentLength - read + headerEnd;
        if (headerLength == 0) {
            throw new RuntimeException("错误的请求");
        }
        bis.reset();
        return headerLength;
    }

    /**
     * Find byte index separating header from body. It must be the last byte of
     * the first two sequential new lines.
     */
    private static int findHeaderEnd(final byte[] buf, int rlen) {
        int splitbyte = 0;
        while (splitbyte + 1 < rlen) {

            // 因为http协议头是空行隔开的；所以最后应该是 \r\n\r\n 结尾的时候就是请求头的数据
            // RFC2616
            if (buf[splitbyte] == '\r'
                    && buf[splitbyte + 1] == '\n'
                    && splitbyte + 3 < rlen    // 防止后面的操作索引不越界
                    && buf[splitbyte + 2] == '\r'
                    && buf[splitbyte + 3] == '\n') {
                return splitbyte + 4;
            }

            // tolerance 兼容
            // 有些程序只使用\n来换行
            if (buf[splitbyte] == '\n' && buf[splitbyte + 1] == '\n') {
                return splitbyte + 2;
            }
            splitbyte++;
        }
        return 0;
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
                    headers.put(els[0].trim(), els[1].trim());
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
        URL resource = WebServer2.class.getClassLoader().getResource("_09/index.html");
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
