package cn.regexp.code.assistant.util;

import cn.hutool.http.HttpUtil;
import cn.regexp.code.assistant.entity.Result;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Regexpei
 * @date 2024/7/20 22:30
 * @description Http 工具类
 * @since 1.0.0
 */
@Slf4j
public class HttpUtils {

    private HttpUtils() {
    }

    public static <T> Result<T> post(String url, Object data) {
        return post(url, data, true);
    }

    public static <T> Result<T> post(String url, Object data, boolean isPrintLog) {
        if (isPrintLog) {
            log.info("url: {}, params: {}", url, JSON.toJSONString(data));
        }

        String resultStr = HttpUtil.post(url, JSON.toJSONString(data));
        Result<T> result = JSON.parseObject(resultStr, new TypeReference<>() {
        });

        if (isPrintLog) {
            log.info("result: {}", JSON.toJSONString(result));
        }
        return result;
    }

}
