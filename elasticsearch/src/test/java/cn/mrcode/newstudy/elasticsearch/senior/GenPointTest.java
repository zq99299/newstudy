package cn.mrcode.newstudy.elasticsearch.senior;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.GeoPolygonQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import java.util.ArrayList;

/**
 * ${todo}
 *
 * @author : zhuqiang
 * @date : 2019/3/16 15:09
 */
public class GenPointTest extends CreateClient {
    @Test
    public void geoBoundingBoxQuery() {
        GeoBoundingBoxQueryBuilder geoBoundingBoxQueryBuilder = QueryBuilders.geoBoundingBoxQuery("pin.location")
                .setCorners(40.73, -74.1, 40.01, -71.12);
        SearchResponse searchResponse = client.prepareSearch("car_shop")
                .setTypes("shops")
                .setQuery(geoBoundingBoxQueryBuilder)
                .get();
        System.out.println(searchResponse);
    }

    @Test
    public void geoPolygonQuery() {
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(new GeoPoint(40.73, -74.1));
        geoPoints.add(new GeoPoint(40.01, -71.12));
        geoPoints.add(new GeoPoint(50.56, -90.58));
        GeoPolygonQueryBuilder query = QueryBuilders.geoPolygonQuery("pin.location", geoPoints);
        SearchResponse searchResponse = client.prepareSearch("car_shop")
                .setTypes("shops")
                .setQuery(query)
                .get();
        System.out.println(searchResponse);
    }

    @Test
    public void geoDistanceQuery() {
        GeoDistanceQueryBuilder query = QueryBuilders.geoDistanceQuery("pin.location")
                .point(40, -70)
                .distance(200, DistanceUnit.KILOMETERS);
        SearchResponse searchResponse = client.prepareSearch("car_shop")
                .setTypes("shops")
                .setQuery(query)
                .get();
        System.out.println(searchResponse);
    }
}
