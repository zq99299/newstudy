package cn.mrcode.newstudy.elasticsearch.senior;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/3/13 23:08
 */
public class BulkTest extends CreateClient {
    @Test
    public void bulk() throws IOException {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        // 要增加一条销售数据
        bulkRequestBuilder
                .add(client.prepareIndex("car_shop", "sales", "3")
                        .setSource(XContentFactory.jsonBuilder()
                                .startObject()
                                .field("brand", "奔驰")
                                .field("name", "奔驰C200")
                                .field("price", 35_0000)
                                .field("produce_date", "2017-01-05") // 生产日期
                                .field("sale_price", 34_0000) // 销售价格
                                .field("sale_date", "2017-02-03") // 销售日期
                                .endObject()
                        )
                );
        // 修改一条数据的价格
        bulkRequestBuilder.add(client.prepareUpdate("car_shop", "sales", "1")
                .setDoc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("price", 29_0000)
                        .endObject())
        );
        // 删除一条数据，之前上传重复的数据
        bulkRequestBuilder.add(client.prepareDelete("car_shop", "sales", "2"));

        BulkResponse bulkItemResponses = bulkRequestBuilder.get();
        if (bulkItemResponses.hasFailures()) {
            System.out.println(bulkItemResponses.buildFailureMessage());
        }

        for (BulkItemResponse item : bulkItemResponses.getItems()) {
            System.out.println(item.getId() + " : " + item.getResponse().getResult());
        }
    }
}
