package cn.mrcode.newstudy.hpbase._09.coreserver;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/05/05     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/5/5 13:37
 * @date 2018/5/5 13:37
 * @since 1.0.0
 */
public interface RequestHandler {
    /**
     * http请求头已经解析好；在该请求方法里面可以根据具体的业务需求进行解析body数据；达到自定义的效果
     * @param request
     */
    void handler(Request request);
}
