package cn.regexp.code.assistant.service;

import cn.hutool.cache.impl.TimedCache;
import cn.regexp.code.assistant.CodeAssistantModule;
import cn.regexp.code.assistant.common.UrlKeyConstant;
import cn.regexp.code.assistant.entity.Result;
import cn.regexp.code.assistant.entity.User;
import cn.regexp.code.assistant.ui.CodeAssistantSettings;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.intellij.openapi.project.Project;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Regexpei
 * @date 2024/7/25 21:02
 * @description 用户 Service
 * @since 1.0.0
 */
@Slf4j
public class UserService extends CommonService {

    /**
     * 用户信息缓存（1分钟）
     */
    private static final TimedCache<String, Map<String, User>> USER_CACHE = new TimedCache<>(1000 * 60);

    public static UserService getInstance() {
        return application.getService(UserService.class);
    }

    /**
     * 获取插件用户
     *
     * @param project 当前项目
     * @return 插件用户
     */
    public List<User> listPluginUser(Project project) {
        Map<String, User> userMap = getFromCache(project);
        return new ArrayList<>(userMap.values());
    }

    /**
     * 获取当前登录用户
     *
     * @param project 当前项目
     * @return 当前登录用户
     */
    public User getCurrentUser(Project project) {
        CodeAssistantSettings codeAssistantSettings = CodeAssistantModule.getInstance(CodeAssistantSettings.class);
        Map<String, User> userMap = getFromCache(project);
        return userMap.getOrDefault(codeAssistantSettings.getUsername(), new User());
    }

    /**
     * 从缓存中获取用户信息 Map，不存在则进行查询
     *
     * @param project 当前项目
     * @return 用户信息 Map（key：用户名，value：用户信息）
     */
    private Map<String, User> getFromCache(Project project) {
        return USER_CACHE.get(project.getName(), () -> {
            String body = get(UrlKeyConstant.LIST_PLUGIN_USER_KEY, null);
            Result<List<User>> result = JSON.parseObject(body, new TypeReference<>() {});
            if (result.isSuccess()) {
                List<User> users = result.getData();
                return users.stream().collect(Collectors.toMap(User::getUsername, Function.identity()));
            }

            log.error("list plugin user error, code: {}, message: {}", result.getCode(), result.getMsg());
            return new HashMap<>();
        });
    }
}
