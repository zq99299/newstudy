package cn.mrcode.newstudy.hpbase._09.filebrowsingserver;

import cn.mrcode.newstudy.hpbase._09.WebServer;
import cn.mrcode.newstudy.hpbase._09.coreserver.HttpServer;
import cn.mrcode.newstudy.hpbase._09.coreserver.Method;
import cn.mrcode.newstudy.hpbase._09.coreserver.Request;
import cn.mrcode.newstudy.hpbase._09.coreserver.RequestHandler;

import java.io.*;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 文件浏览服务
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/05/05     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/5/5 13:45
 * @date 2018/5/5 13:45
 * @since 1.0.0
 */
public class FileBrowsingServer implements RequestHandler {
    public static void main(String[] args) throws IOException {
        String rootPath = "F:\\dev\\project\\newstudy\\hp-base\\src\\test\\java\\cn\\mrcode\\newstudy\\hpbase";
        rootPath = "d:/";
        new HttpServer().start(6039, new FileBrowsingServer(Paths.get(rootPath)));
    }


    private Charset UTF8 = Charset.forName("UTF-8");
    private final String CRLF = "\r\n";
    private final Path dir;
    public static Map<String, String> mimeTyps;

    /**
     * 要展示的文件根目录
     * @param dir
     */
    public FileBrowsingServer(Path dir) {
        Objects.requireNonNull(dir);
        this.dir = dir;
        Properties pro = new Properties();
        String rootDir = WebServer.class.getClassLoader().getResource("_09").getFile().substring(1);
        try {
            pro.load(Files.newInputStream(Paths.get(rootDir, "default-mimetypes.properties")));
            mimeTyps = (Map) pro;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        dirs = Arrays.stream(dirPaths).filter(Files::isDirectory).collect(Collectors.toList());
    }

    @Override
    public void handler(Request request) {
        try {
            Method method = request.getMethod();
            if (method != Method.GET) {
                OutputStream output = request.getOutput();
                byte[] bodys = "只支持GET请求".getBytes(UTF8);
                StringBuilder res = new StringBuilder();
                res.append("HTTP/1.1 200 OK").append(CRLF)
                        .append("Date: " + new Date()).append(CRLF)
                        .append("Content-Type: text/html;charset=utf-8").append(CRLF)
                        .append("Content-Length:" + bodys.length).append(CRLF)
                        .append(CRLF);
                output.write(res.toString().getBytes(UTF8));
                output.write(bodys);
                output.flush();
                return;
            }

            String uri = request.getUri();
            /**
             * <ul>
             <li><a href=""></a></li>
             </ul>
             */

            if ("/".equals(uri)) {
                StringBuilder res = buildList(dir, "/");
                response(request, res.toString());
            } else {
                Path target = dir.resolve(uri.substring(1));
                if (!Files.exists(target)) {
                    return;
                }
                if (Files.isDirectory(target)) {
                    response(request, buildList(target, uri).toString());
                    return;
                }

                String mimeTyp = URLConnection.guessContentTypeFromName(request.getUri());
                if (mimeTyp == null) {
                    mimeTyp = getMimeType(target);
                }
                // 全部用来下载；文件编码不太好知道，还比较麻烦；这里就不深入了
                mimeTyp = null;
                response(request, target, mimeTyp == null ? "application/octet-stream" : mimeTyp);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMimeType(Path target) {
        String fileName = target.getFileName().toString();
        int i = fileName.lastIndexOf(".");
        String ct = null;
        if (i != -1) {
            String type = fileName.substring(i + 1);
            ct = mimeTyps.get(type.toLowerCase());
            if (ct != null && ct.equals("text/x-java-source, text/java")) {
                ct += ";charset=utf-8";
            }
        }
        return ct;
    }

    private StringBuilder buildList(Path targetDir, String parent) throws IOException {
        if (parent.charAt(parent.length() - 1) != '/') {
            parent += '/';
        }
        StringBuilder res = new StringBuilder();
        res.append("<ul>");
        if (!parent.equals("/")) {
            String url = parent + "..";
            url = urlencoder(url);
            String info = ".. ↶";
            res.append("<li><a style=\"font-size: 50px;\" href=\"" + url + "\">" + info + "</a></li>").append(CRLF);
        }
        final String finalParent = parent;
        Files.list(targetDir)
                .forEach(p -> {
                    try {
                        String url = finalParent + p.getFileName();
                        String info = url;
                        url = urlencoder(url);
                        if (Files.isDirectory(p)) {
                            res.append("<li><a class=\"text-white bg-dark\" href=\"" + url + "\">" + info + "</a></li>").append(CRLF);
                        } else {
                            info += "\t (" + Files.size(p) / 1024 + " KB)";
                            res.append("<li><a class=\"text-dark\" href=\"" + url + "\">" + info + "</a></li>").append(CRLF);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        res.append("</ul>");
        return res;
    }

    /**
     * url编码需要自己处理路径符，否则会一起编码的
     * @param url
     * @return
     */
    private String urlencoder(String url) {
        StringTokenizer st = new StringTokenizer(url, "/");
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            try {
                sb.append("/").append(URLEncoder.encode(st.nextToken(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void response(Request request, String body) throws IOException {
        StringBuilder bodySb = new StringBuilder();
        bodySb.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<meta charset=\"utf-8\">")
                .append("<title>文件浏览</title>")
                .append("<link rel=\"stylesheet\" href=\"https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">")
                .append("</head>")
                .append("<body class=\"container\">")
                .append(body)
                .append("</body>")
                .append("</html>");
        byte[] bodys = bodySb.toString().getBytes(UTF8);
        StringBuilder res = new StringBuilder();
        res.append("HTTP/1.1 200 OK").append(CRLF)
                .append("Date: " + new Date()).append(CRLF)
                .append("Content-Type: text/html;charset=utf-8").append(CRLF)
                .append("Content-Length:" + bodys.length).append(CRLF)
                .append(CRLF);
        OutputStream output = request.getOutput();
        output.write(res.toString().getBytes(UTF8));
        output.write(bodys);
        output.flush();
    }

    public void response(Request request, Path path, String contentType) throws IOException {
        long size = Files.size(path);
        StringBuilder res = new StringBuilder();
        res.append("HTTP/1.1 200 OK").append(CRLF)
                .append("Date: " + new Date()).append(CRLF)
                .append("Content-Type:").append(contentType).append(CRLF)
                .append("Content-Length:" + size).append(CRLF)
                .append(CRLF);
        OutputStream output = request.getOutput();
        output.write(res.toString().getBytes(UTF8));
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path.toFile()));) {
            int b = -1;
            while ((b = bis.read()) != -1) {
                output.write(b);
            }
            output.flush();
        }
    }
}
