package cn.mrcode.newstudy.elasticsearch.senior;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/3/16 13:52
 */
public class ScrollCarTest extends CreateClient {

    @Test
    public void scrollTest() {
        SearchResponse searchResponse = client.prepareSearch("car_shop")
                .setTypes("sales")
                .setScroll(TimeValue.timeValueSeconds(60))
                .setQuery(QueryBuilders.termQuery("brand.keyword", "宝马"))
                .setSize(1)
                .get();

        do {
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                System.out.println(hit.getSourceAsString());
            }

            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(new TimeValue(60000))
                    .execute()
                    .actionGet();
        } while (searchResponse.getHits().getHits().length != 0);
    }
}
