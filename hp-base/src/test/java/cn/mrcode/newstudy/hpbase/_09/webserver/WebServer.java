package cn.mrcode.newstudy.hpbase._09.webserver;

import cn.mrcode.newstudy.hpbase._09.coreserver.HttpServer;
import cn.mrcode.newstudy.hpbase._09.coreserver.Method;
import cn.mrcode.newstudy.hpbase._09.coreserver.Request;
import cn.mrcode.newstudy.hpbase._09.coreserver.RequestHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

/**
 * 实现一个处理请求的简单响应的web容器
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/05/05     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/5/5 15:43
 * @date 2018/5/5 15:43
 * @since 1.0.0
 */
public class WebServer implements RequestHandler {
    public static final String CRLF = "\r\n";
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static String rootDir = WebServer.class.getClassLoader().getResource("_09/webserver").getFile().substring(1);

    public static void main(String[] args) throws IOException {
        new HttpServer().start(6040, new WebServer());
    }

    @Override
    public void handler(Request request) {
        Method method = request.getMethod();
        switch (method) {
            case GET:
                get(request);
                break;
            case POST:
                post(request);
                break;
            default:
                unsupported(request);
                break;
        }
    }

    private void unsupported(Request request) {

    }

    private void post(Request request) {
        if (MultipartFormDataParse.isMultipartFormData(request)) {
            List<MultipartItem> multipartItems = new MultipartFormDataParse(request).parse();
            for (MultipartItem multipartItem : multipartItems) {
                System.out.println(multipartItem);
            }
        }
    }

    private void get(Request request) {
        String uri = request.getUri();
        if ("/".equals(uri)) {
            resView(request.getOutput(), "index.html");
        }
    }

    private static void resView(OutputStream outputStream, String targetHtml) {
        Path path = Paths.get(rootDir, targetHtml);
        File file = path.toFile();
        try (FileInputStream reader = new FileInputStream(file);) {
            StringBuilder res = new StringBuilder();
            res.append("HTTP/1.1 200 OK\r\n")
                    .append("Date: " + new Date()).append(CRLF)
                    .append("Content-Type: text/html;charset=utf-8").append(CRLF)
                    .append("Content-Length:" + file.length()).append(CRLF)
                    .append(CRLF);
            outputStream.write(res.toString().getBytes("utf-8"));
            byte[] buffer = new byte[reader.available()];
            reader.read(buffer);
            outputStream.write(buffer);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
