package cn.mrcode.newstudy.elasticsearch;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/1/22 21:59
 */
public class DemoTest {
    private TransportClient client = null;

    @Before
    public void createClient() throws UnknownHostException {
        // 集群连接
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
//                // 在同一台机器上面启动多个实例，端口会变化,多个地址在这里添加
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9301))
        ;
    }

    @Test
    public void createEmployee() throws IOException {
        IndexResponse response = client.prepareIndex("company", "employee", "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "jack")
                        .field("age", 27)
                        .field("position", "technique")
                        .field("country", "china")
                        .field("join_date", "2017-01-01")
                        .field("salary", 10000)
                        .endObject())
                .get();
        System.out.println(response.getResult());
    }

    // 按 id 查询文档
    @Test
    public void getById() {
        // 很奇葩的一个现象，执行后该对象 toString 方法是一个错误栈，实际上是可以获取到数据的
        // Error building toString out of XContent: com.fasterxml.jackson.core.JsonGenerationException: Can not start an object, expecting field name (context: Object)
        GetResponse response = client.prepareGet("company", "employee", "1").get();
        System.out.println(response.getSource());
    }

    @Test
    public void update() throws IOException {
        UpdateResponse updateResponse = client.prepareUpdate("company", "employee", "1")
                .setDoc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("age", "26")
                        .endObject())
                .get();
        System.out.println(updateResponse);
    }

    @Test
    public void delete() {
        DeleteResponse response = client.prepareDelete("company", "employee", "1").get();
        System.out.println(response);
    }
}