package cn.mrcode.newstudy.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 聚合分析
 *
 * @author : zhuqiang
 * @date : 2019/1/22 23:18
 */
public class EmployeeAggrTest {
    private TransportClient client = null;

    @Before
    public void createClient() throws UnknownHostException {
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
        ;
    }

    /**
     * <pre>
     * 需求：
     * （1）首先按照 country 国家来进行分组
     * （2）然后在每个 country 分组内，再按照入职年限进行分组
     * （3）最后计算每个分组内的平均薪资
     * </pre>
     */
    @Test
    public void aggr() throws ExecutionException, InterruptedException {
        SearchResponse searchResponse = client.prepareSearch("company")
                .setTypes("employee")
                .addAggregation(
                        AggregationBuilders
                                // 前面的是对该操作取名，后面的是真实的字段
                                .terms("group_by_country")
                                .field("country")
                                .subAggregation(
                                        AggregationBuilders
                                                .dateHistogram("group_by_join_date")
                                                .field("join_date")
                                                .dateHistogramInterval(DateHistogramInterval.YEAR) // 按照年来分
                                                .subAggregation(
                                                        AggregationBuilders
                                                                .avg("ave_salary")
                                                                .field("salary")
                                                )
                                )
                )
                .execute()
                .get();
        System.out.println(searchResponse);

        // 怎么用 api 来获取里面的分组结果数据呢？
        // 这个只能看着结果，debug 来获取到层级对象

        // 它的类型和之前查询的类型对应
        StringTerms groupByCountry = (StringTerms) searchResponse.getAggregations().asMap().get("group_by_country");
        List<Terms.Bucket> buckets = groupByCountry.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("==== " + keyAsString);
            InternalDateHistogram groupByJoinDate = (InternalDateHistogram) bucket.getAggregations().asMap().get("group_by_join_date");
            List<Histogram.Bucket> groupByJoinDateBuckets = groupByJoinDate.getBuckets();
            for (Histogram.Bucket groupByJoinDateBucket : groupByJoinDateBuckets) {
                System.out.println("===== " + groupByJoinDateBucket.getKeyAsString());
                InternalAvg aveSalary = (InternalAvg) groupByJoinDateBucket.getAggregations().asMap().get("ave_salary");
                System.out.println("======" + aveSalary.getValueAsString());
            }
        }
        System.out.println();
    }
}
