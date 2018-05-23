package cn.mrcode.newstudy.hpbase._10;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/5/22 21:40
 */
public class NioChatServer {
    public static void main(String[] args) throws Exception {
        new NioChatServer().init(6000);
    }

    private Selector selector;
    private Charset charset = Charset.forName("utf-8");
    private String userName = "系统";
    // 用来存储所有已登录用户
    private Map<String, SocketChannel> users = new HashMap<>();

    public void init(int port) throws Exception {
        // 多路复用器
        selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress isa = new InetSocketAddress(port);
        ssc.bind(isa);
        ssc.configureBlocking(false); // 非阻塞
        // ServerSocketChannel 只支持 accept事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        // 没有事件则会阻塞
        while (selector.select() > 0) {
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                it.remove(); // 如果处理一个事件，不移除的话，就会导致出现异常等。原理待分析
                // 如果是链接请求
                if (sk.isAcceptable()) {
                    accept(sk);
                    continue;
                }
                if (sk.isReadable()) {
                    readable(sk);
                    continue;
                }
            }
        }
    }

    private void accept(SelectionKey sk) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
//            sk.interestOps(SelectionKey.OP_ACCEPT);
            // 发送登录请求
            ChatRespones chatRespones = new ChatRespones(ChatRequest.TYPE_LOGIN);
            chatRespones.setSuccess(false);
            chatRespones.setError("请输入您的用户名登录聊天室");
            chatRespones.setFrom("系统");
            sc.write(charset.encode(JSON.toJSONString(chatRespones)));
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readable(SelectionKey sk) {
        // 客户端断开链接，为什么会是一个读事件？
        try {
            SocketChannel sc = (SocketChannel) sk.channel();
//            sc.register(selector, SelectionKey.OP_READ);
            ChatRequest chatRequest = getChatRequest(sc);
            byte type = chatRequest.getType();
            switch (type) {
                case ChatRequest.TYPE_LOGIN:
                    doLogin(chatRequest, sc);
                    break;
                case ChatRequest.TYPE_ROOM:
                    doRoom(chatRequest);
                    break;
                case ChatRequest.TYPE_PRIVATE:
                    doPrivate(chatRequest);
                    break;
            }
            sk.interestOps(SelectionKey.OP_READ);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果该客户端出现了异常，则标识有可能客户端断开了链接
            sk.cancel();
            IOUtils.closeQuietly(sk.channel());
        }
    }


    private void doLogin(ChatRequest chatRequest, SocketChannel sc) throws IOException {
        String user = chatRequest.getUser();
        if (users.containsKey(user)) {
            ChatRespones chatRespones = new ChatRespones(ChatRequest.TYPE_LOGIN);
            chatRespones.setSuccess(false);
            chatRespones.setError("用户名已存在，请更换一个用户名");
            chatRespones.setFrom(userName);
            sc.write(charset.encode(JSON.toJSONString(chatRespones)));
        } else {
            users.put(user, sc);
            ChatRespones chatRespones = new ChatRespones(ChatRequest.TYPE_LOGIN);
            chatRespones.setSuccess(true);
            StringBuilder info = new StringBuilder();
            info.append("欢迎加入聊天室").append("\r\n")
                    .append(getRoomOnline())
                    .append("对指定人说话可以使用@用户名 信息 的方式");
            chatRespones.setInfo(info.toString());
            chatRespones.setFrom(userName);
            sc.write(charset.encode(JSON.toJSONString(chatRespones)));

            sendSysRoom(getRoomOnline(), user);
        }
    }

    private void doRoom(ChatRequest chatRequest) {
        String from = chatRequest.getFrom();
        ChatRespones chatRespones = new ChatRespones(ChatRequest.TYPE_ROOM);
        chatRespones.setSuccess(true);
        chatRespones.setFrom(from);
        chatRespones.setInfo(chatRequest.getInfo());

        users.forEach((u, socketChannel) -> {
            if (from.equals(u)) {
                return;
            }
            chatRespones.setTo(u);
            ByteBuffer info = charset.encode(JSON.toJSONString(chatRespones));
            try {
                socketChannel.write(info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void doPrivate(ChatRequest chatRequest) throws IOException {
        String from = chatRequest.getFrom();
        ChatRespones chatRespones = new ChatRespones(ChatRequest.TYPE_PRIVATE);
        chatRespones.setSuccess(true);
        chatRespones.setFrom(from);
        chatRespones.setInfo(chatRequest.getInfo());
        String to = chatRequest.getTo();
        chatRespones.setTo(to);
        if (users.containsKey(to)) {
            SocketChannel socketChannel = users.get(to);
            ByteBuffer info = charset.encode(JSON.toJSONString(chatRespones));
            socketChannel.write(info);
        } else {
            ChatRespones err = new ChatRespones(ChatRequest.TYPE_PRIVATE);
            chatRespones.setSuccess(false);
            chatRespones.setTo(from);
            chatRespones.setError(to + " 已离线，消息未送达！");
            chatRespones.setFrom(userName);
            ByteBuffer info = charset.encode(JSON.toJSONString(err));
            SocketChannel socketChannel = users.get(from);
            socketChannel.write(info);
        }
    }

    /**
     * 发送聊天室的系统消息
     * @param info
     * @param filterUser 过滤某个用户名
     */
    private void sendSysRoom(String info, String filterUser) {
        ChatRespones chatRespones = new ChatRespones(ChatRequest.TYPE_ROOM);
        chatRespones.setSuccess(true);
        chatRespones.setFrom(userName);
        chatRespones.setInfo(info);
        users.forEach((u, socketChannel) -> {
            if (filterUser != null && filterUser.equals(u)) {
                return;
            }
            chatRespones.setTo(u);
            try {
                socketChannel.write(charset.encode(JSON.toJSONString(chatRespones)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    // 获取聊天室在线用户列表
    private String getRoomOnline() {
        StringBuilder sb = new StringBuilder();
        sb.append("目前在线用户列表：").append("\r\n");
        for (String s : users.keySet()) {
            sb.append("   ").append(s).append("\r\n");
        }
        return sb.toString();
    }

    private ChatRequest getChatRequest(SocketChannel sc) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5);

        // 客户端发送5个byte。在这里居然可以收到8个byte
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 如果读取不完数据，则一直读取
        while (sc.read(buffer) > 0) {
            buffer.flip(); // 重置下，因为解码是要从你读到数据的开始解码
            // 这里要使用这种方式，不然获取到的数据 都是脏数据
            while (buffer.hasRemaining()) {
                baos.write(buffer.get());
            }
            buffer.clear();
        }
        CharBuffer decode = charset.decode(ByteBuffer.wrap(baos.toByteArray()));
        return JSON.parseObject(decode.toString(), ChatRequest.class);
    }
}
