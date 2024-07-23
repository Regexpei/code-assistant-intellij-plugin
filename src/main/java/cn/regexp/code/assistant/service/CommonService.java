package cn.regexp.code.assistant.service;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.regexp.code.assistant.CodeAssistantModule;
import cn.regexp.code.assistant.entity.Result;
import cn.regexp.code.assistant.ui.CodeAssistantSettings;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;

import static cn.regexp.code.assistant.common.Constant.COMMON_RESOURCE;

/**
 * @author Regexpei
 * @date 2024/7/23 20:02
 * @description 公用 Service
 * @since 1.0.0
 */
public class CommonService {

    /**
     * 获取 Application
     */
    public static Application application = ApplicationManager.getApplication();

    public <T> Result<T> post(String urlKey, Object data) {
        // 获取配置
        CodeAssistantSettings codeAssistantSettings = CodeAssistantModule.getInstance(CodeAssistantSettings.class);

        // 获取 url
        String url = codeAssistantSettings.getHost() + COMMON_RESOURCE.getString(urlKey);
        // 创建并执行请求
        HttpResponse response = HttpUtil.createPost(url)
                .header("Authorization", "Bearer " + codeAssistantSettings.getToken())
                .body(JSON.toJSONString(data))
                .execute();

        if (!response.isOk()) {
            throw new RuntimeException(String.format("code: %d, cookieStr: %s",
                    response.getStatus(), response.getCookieStr()));
        }
        return JSON.parseObject(response.body(), new TypeReference<>() {});
    }
}
