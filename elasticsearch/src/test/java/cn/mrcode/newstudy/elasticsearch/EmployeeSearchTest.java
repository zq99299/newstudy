package cn.mrcode.newstudy.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/1/22 22:55
 */
public class EmployeeSearchTest {
    private TransportClient client = null;

    @Before
    public void createClient() throws UnknownHostException {
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
        ;
    }

    /**
     * 准备数据
     */
    @Test
    public void prepareData() throws Exception {
        client.prepareIndex("company", "employee", "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "jack")
                        .field("age", 27)
                        .field("position", "technique software")
                        .field("country", "china")
                        .field("join_date", "2017-01-01")
                        .field("salary", 10000)
                        .endObject())
                .get();

        client.prepareIndex("company", "employee", "2")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "marry")
                        .field("age", 35)
                        .field("position", "technique manager")
                        .field("country", "china")
                        .field("join_date", "2017-01-01")
                        .field("salary", 12000)
                        .endObject())
                .get();

        client.prepareIndex("company", "employee", "3")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "tom")
                        .field("age", 32)
                        .field("position", "senior technique software")
                        .field("country", "china")
                        .field("join_date", "2016-01-01")
                        .field("salary", 11000)
                        .endObject())
                .get();

        client.prepareIndex("company", "employee", "4")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "jen")
                        .field("age", 25)
                        .field("position", "junior finance")
                        .field("country", "usa")
                        .field("join_date", "2016-01-01")
                        .field("salary", 7000)
                        .endObject())
                .get();

        client.prepareIndex("company", "employee", "5")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "mike")
                        .field("age", 37)
                        .field("position", "finance manager")
                        .field("country", "usa")
                        .field("join_date", "2015-01-01")
                        .field("salary", 15000)
                        .endObject())
                .get();
    }

    /**
     * <pre>
     * 搜索：需求如下
     * （1）搜索职位中包含 technique 的员工
     * （2）同时要求 age 在 30 到 40 岁之间
     * （3）分页查询，查找第一页
     * </pre>
     */
    @Test
    public void search() {
        SearchResponse searchResponse = client.prepareSearch("company")
                .setTypes("employee")
                .setQuery(QueryBuilders.matchQuery("position", "technique"))
                .setPostFilter(QueryBuilders.rangeQuery("age").from(30).to(40))
                .setFrom(0)
                .setSize(1)
                .get();
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
    }
}
