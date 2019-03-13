package cn.mrcode.newstudy.elasticsearch.senior;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
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
import java.util.concurrent.ExecutionException;


/**
 * upsert 调整汽车价格
 *
 * @author : zhuqiang
 * @date : 2019/3/13 22:19
 */
public class UpsertCarTest extends CreateClient{

    @Test
    public void upsertCar() throws IOException, ExecutionException, InterruptedException {
        // 当 id=1 不存在的时候新增，存在的时候更新汽车价格

        IndexRequest upsertRequest = new IndexRequest("car_shop", "cars", "1");
        upsertRequest.source(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("brand", "宝马")
                        .field("name", "宝马320")
                        .field("price", 320000)
                        .field("produce_date", "2017-01-01")
                        .endObject());

        UpdateRequest updateRequest = new UpdateRequest("car_shop", "cars", "1");
        updateRequest.doc(XContentFactory.jsonBuilder()
                .startObject()
                .field("price", 31_0000)
                .endObject())
                .upsert(upsertRequest);  // 更新的时候关联一个 upsert

        ActionFuture<UpdateResponse> update = client.update(updateRequest);
        System.out.println(update.get());
    }
}
