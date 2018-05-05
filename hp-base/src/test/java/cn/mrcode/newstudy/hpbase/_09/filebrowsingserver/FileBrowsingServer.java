package cn.mrcode.newstudy.hpbase._09.filebrowsingserver;

import cn.mrcode.newstudy.hpbase._09.WebServer;
import cn.mrcode.newstudy.hpbase._09.coreserver.HttpServer;
import cn.mrcode.newstudy.hpbase._09.coreserver.Method;
import cn.mrcode.newstudy.hpbase._09.coreserver.Request;
import cn.mrcode.newstudy.hpbase._09.coreserver.RequestHandler;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

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
        new HttpServer().start(6039, new FileBrowsingServer(Paths.get("F:\\dev\\project\\newstudy\\hp-base\\src\\test\\java\\cn\\mrcode\\newstudy\\hpbase")));
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

                String fileName = target.getFileName().toString();
                int i = fileName.lastIndexOf(".");
                String ct = null;
                if (i != -1) {
                    String type = fileName.substring(i + 1);
                    ct = mimeTyps.get(type.toLowerCase());
                    if (ct.equals("text/x-java-source, text/java")) {
                        ct += ";charset=utf-8";
                    }
                }
                response(request, target, ct == null ? "application/octet-stream" : ct);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder buildList(Path targetDir, String parent) throws IOException {
        if (parent.charAt(parent.length() - 1) != '/') {
            parent += '/';
        }
        StringBuilder res = new StringBuilder();
        res.append("<ul>");
        if (!parent.equals("/")) {
            String url = parent + "..";
            String info = "..";
            res.append("<li><a href=\"" + url + "\">" + info + "</a></li>").append(CRLF);
        }
        final String finalParent = parent;
        Files.list(targetDir)
                .forEach(p -> {
                    try {
                        String url = finalParent + p.getFileName();
                        String info = null;
                        if (Files.isDirectory(p)) {
                            info = url + "\t (目录)";
                        } else {
                            info = url + "\t (" + Files.size(p) + "byte)";
                        }
                        res.append("<li><a href=\"" + url + "\">" + info + "</a></li>").append(CRLF);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        res.append("</ul>");
        return res;
    }

    public void response(Request request, String body) throws IOException {
        byte[] bodys = body.getBytes(UTF8);
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
