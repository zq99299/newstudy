package cn.mrcode.newstudy.elasticsearch.senior;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/3/13 21:49
 */
public class DemoTest {
    // 集群自动嗅探
    @Test
    public void sinff() throws UnknownHostException {

        Settings settings = Settings.builder()
                .put("client.transport.sniff", true).build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

    }
}
