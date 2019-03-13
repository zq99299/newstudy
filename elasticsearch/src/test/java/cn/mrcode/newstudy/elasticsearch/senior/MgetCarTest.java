package cn.mrcode.newstudy.elasticsearch.senior;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.junit.Test;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/3/13 22:54
 */
public class MgetCarTest extends CreateClient {
    @Test
    public void mget() {
        MultiGetResponse responses = client.prepareMultiGet()
                .add("car_shop", "cars", "1")
                .add("car_shop", "cars", "2")
                .get();
        responses.forEach(item -> {
                    GetResponse response = item.getResponse();
                    if (response.isExists()) {
                        System.out.println(response.getSourceAsString());
                    }
                }
        );
    }
}
