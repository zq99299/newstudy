package cn.mrcode.newstudy.elasticsearch.senior;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/3/16 14:40
 */
public class FullTextSearchText extends CreateClient {
    /**
     * 按品牌名搜索
     */
    @Test
    public void searchByBrand() {
        SearchResponse response = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(QueryBuilders.matchQuery("brand", "宝马"))
                .get();
        System.out.println(response);
    }

    /**
     * 多字段搜索
     */
    @Test
    public void multiMatchQuery() {
        SearchResponse response = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(QueryBuilders.multiMatchQuery("宝马", "brand", "name"))
                .get();
        System.out.println(response);
    }

    /**
     * terms 搜索
     */
    @Test
    public void commonTermsQuery() {
        SearchResponse response = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(QueryBuilders.commonTermsQuery("name", "宝马320"))
                .get();
        System.out.println(response);
    }

    /**
     * 前缀搜索
     */
    @Test
    public void prefixQuery() {
        SearchResponse response = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(QueryBuilders.prefixQuery("name", "宝"))
                .get();
        System.out.println(response);
    }
}
