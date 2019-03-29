package cn.mrcode.newstudy.elasticsearch.senior;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

import java.util.HashMap;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/3/16 14:14
 */
public class PageQueryCarTest extends CreateClient {
    @Test
    public void pageQueryTest() {
        HashMap<String, Object> scriptParams = new HashMap<>();
        scriptParams.put("from", 0);
        scriptParams.put("size", 1);
        scriptParams.put("brand", "宝马");

        SearchTemplateResponse searchTemplateResponse = new SearchTemplateRequestBuilder(client)
                .setScriptType(ScriptType.FILE)
                .setScript("page-query-by-brand")  // page-query-by-brand.mustache
                .setScriptParams(scriptParams)
                .setRequest(new SearchRequest("car_shop").types("sales"))
                .get();
        SearchResponse response = searchTemplateResponse.getResponse();
        System.out.println(searchTemplateResponse.getResponse());
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        client.close();
    }
}
