package cn.mrcode.newstudy.elasticsearch.senior;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/3/13 22:54
 */
public class CreateClient {
    public TransportClient client;

    @Before
    public void before() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch") // 集群名称
                .put("client.transport.sniff", true) // 自动探查
                .build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }
}
