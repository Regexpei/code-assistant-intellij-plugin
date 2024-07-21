package cn.regexp.code.assistant.ui;

import com.google.common.base.Optional;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Regexpei
 * @date 2024/7/20 20:45
 * @description CodeAssistantSettings
 * @since 1.0.0
 */
@Setter
@Getter
@Slf4j
@State(name = "CodeAssistantSettings", storages = @Storage("code_assistant_settings.xml"))
public class CodeAssistantSettings implements PersistentStateComponent<Element> {

    private static final String CODE_ASSISTANT_SETTINGS_TAG = "CodeAssistantSettings";

    private static final String HOST = "Host";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String TOKEN = "Token";

    private String host = "";
    private String username = "";

    private static final String CODE_ASSISTANT_SETTINGS_PASSWORD_KEY = "CODE_ASSISTANT_SETTINGS_PASSWORD_KEY";
    private static final String CODE_ASSISTANT_SETTINGS_TOKEN_KEY = "CODE_ASSISTANT_SETTINGS_TOKEN_KEY";

    private static final CredentialAttributes CREDENTIAL_ATTRIBUTES_PASSWORD = new CredentialAttributes(
            CodeAssistantSettings.class.getName(), CODE_ASSISTANT_SETTINGS_PASSWORD_KEY);
    private static final CredentialAttributes CREDENTIAL_ATTRIBUTES_TOKEN = new CredentialAttributes(
            CodeAssistantSettings.class.getName(), CODE_ASSISTANT_SETTINGS_TOKEN_KEY);


    /**
     * 只有与默认值不同的值才会被序列化。
     * 空值表示将不存储当前值，而是保持原来的序列化的值。
     *
     * @return 组件状态。所有属性、公共和带注释的字段都被序列化。
     */
    @Override
    public @Nullable Element getState() {
        Element element = new Element(CODE_ASSISTANT_SETTINGS_TAG);
        element.setAttribute(HOST, (getHost() != null ? getHost() : ""));
        element.setAttribute(USERNAME, (getUsername() != null ? getUsername() : ""));
        return element;
    }

    /**
     * 当打开配置时，会调用该方法获取配置文件中的配置值。
     * 当配置文件在 IDE 运行时被外部修改了，则该方法可能会被调用多次。
     * element 可以直接进行使用，不需要进行防御性复制。
     *
     * @param element 配置文件信息
     */
    @Override
    public void loadState(@NotNull Element element) {
        try {
            setHost(element.getAttributeValue(HOST));
            setUsername(element.getAttributeValue(USERNAME));
        } catch (Exception e) {
            log.error("Error happened while loading code assistant settings: " + e);
        }
    }

    public void setPassword(String password) {
        // 将密码设置为 Credentials 对象
        PasswordSafe.getInstance()
                .set(CREDENTIAL_ATTRIBUTES_PASSWORD, new Credentials(null, password != null ? password : ""));
    }

    public String getPassword() {
        if (!ApplicationManager.getApplication().isDispatchThread()) {
            if (preloadedPassword == null) {
                throw new IllegalStateException("Need to call #preloadPassword when password is required in background thread");
            }
        } else {
            preloadPassword();
        }
        return preloadedPassword.or("");
    }

    public void preloadPassword() {
        Credentials credentials = PasswordSafe.getInstance().get(CREDENTIAL_ATTRIBUTES_PASSWORD);
        String password = credentials != null ? credentials.getPasswordAsString() : null;
        preloadedPassword = Optional.fromNullable(password);
    }

    private Optional<String> preloadedPassword;


    public void setToken(String token) {
        // 将 Token 设置为 Credentials 对象
        PasswordSafe.getInstance()
                .set(CREDENTIAL_ATTRIBUTES_TOKEN, new Credentials(null, token != null ? token : ""));
    }

    public String getToken() {
        // 获取 Credentials 对象
        Credentials credentials = PasswordSafe.getInstance().get(CREDENTIAL_ATTRIBUTES_TOKEN);
        // 获取 Token
        return credentials != null ? credentials.getPasswordAsString() : null;
    }

}
