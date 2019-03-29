package cn.mrcode.newstudy.elasticsearch.senior;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/3/16 14:54
 */
public class BoolQueryTest extends CreateClient {
    @Test
    public void boolQueryTest() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("brand", "宝马"))
                .mustNot(QueryBuilders.termQuery("name.keyword", "宝马318"))
                .should(QueryBuilders.termQuery("produce_date", "2017-01-02"))
                .filter(QueryBuilders.rangeQuery("price").gt(280000).lt(350000));

        SearchResponse searchResponse = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(boolQueryBuilder)
                .get();
        System.out.println(searchResponse);
    }
}
