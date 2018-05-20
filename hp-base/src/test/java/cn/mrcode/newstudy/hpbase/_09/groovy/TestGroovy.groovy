package cn.mrcode.newstudy.hpbase._09.groovy

/**
 * ${todo}* @author : zhuqiang
 * @version : V1.0
 *
 * @date : 2018/5/20 15:04
 */
class TestGroovy {
    long getTime(Date date) {
        return date.getTime();
    }

    Date getDate(long time) {
        return new Date(time);
    }
}
