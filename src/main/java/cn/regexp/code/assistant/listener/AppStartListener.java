package cn.regexp.code.assistant.listener;

import cn.hutool.core.util.StrUtil;
import cn.regexp.code.assistant.CodeAssistantModule;
import cn.regexp.code.assistant.entity.Result;
import cn.regexp.code.assistant.ui.CodeAssistantSettings;
import cn.regexp.code.assistant.util.HttpUtils;
import com.intellij.ide.AppLifecycleListener;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Regexpei
 * @date 2024/7/24 23:30
 * @description 应用启动监听器
 * @since 1.0.0
 */
@Slf4j
public class AppStartListener implements AppLifecycleListener {

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        CodeAssistantSettings codeAssistantSettings = CodeAssistantModule.getInstance(CodeAssistantSettings.class);
        String host = codeAssistantSettings.getHost();
        String username = codeAssistantSettings.getUsername();
        String password = codeAssistantSettings.getPassword();

        if (!StrUtil.isAllNotBlank(host, username, password)) {
            // ToDo 未登录，提醒用户登录
            return;
        }

        Map<String, String> authInfoMap = new HashMap<>();
        authInfoMap.put("username", username);
        authInfoMap.put("password", password);

        String url = host + "/plugin/user/login";
        try {
            Result<String> result = HttpUtils.post(url, authInfoMap);
            if (result.isSuccess()) {
                // 登录成功
                codeAssistantSettings.setToken(result.getData());
            }
        } catch (Exception ex) {
            log.error("Login failed: {}", ex.getMessage(), ex);
        }
    }
}
