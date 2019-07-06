package com.lamarsan.zookeeper.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * className: JsonUtil
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 21:45
 */
public class JsonUtil {
    public static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * JSON 转 POJO
     * @param pojo
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getObject(String pojo, Class<T> tClass) {
        try {
            return JSONObject.parseObject(pojo, tClass);
        } catch (Exception e) {
            logger.error(tClass + "转 JSON 失败");
        }
        return null;
    }

    /**
     * pojo 转json
     * @param tResponse
     * @param <T>
     * @return
     */
    public static <T> String getJson(T tResponse) {
        String pojo = JSONObject.toJSONString(tResponse);
        return pojo;
    }
}
