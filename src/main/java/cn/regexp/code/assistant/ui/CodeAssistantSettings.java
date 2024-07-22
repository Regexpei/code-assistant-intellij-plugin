package cn.regexp.code.assistant.ui;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Regexpei
 * @date 2024/7/20 20:45
 * @description CodeAssistantSettings
 * @since 1.0.0
 */
@Setter
@Getter
@Slf4j
// 定义持久化数据的信息，name 为根标签名称，storages 为存储位置
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

    private static final CredentialAttributes CREDENTIAL_ATTRIBUTES_PASSWORD =
            new CredentialAttributes(CodeAssistantSettings.class.getName(), CODE_ASSISTANT_SETTINGS_PASSWORD_KEY);
    private static final CredentialAttributes CREDENTIAL_ATTRIBUTES_TOKEN =
            new CredentialAttributes(CodeAssistantSettings.class.getName(), CODE_ASSISTANT_SETTINGS_TOKEN_KEY);


    /**
     * 只有与默认值不同的值才会被序列化。
     * 空值表示将不存储当前值，而是保持原来的序列化的值。
     *
     * @return 组件状态。所有属性、公共和带注释的字段都被序列化。
     */
    @Override
    public @Nullable Element getState() {
        Element element = new Element(CODE_ASSISTANT_SETTINGS_TAG);
        element.setAttribute(HOST, Optional.ofNullable(this.host).orElse(""));
        element.setAttribute(USERNAME, Optional.ofNullable(this.username).orElse(""));
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

    public void setPassword(String username, String password) {
        // 将密码设置为 Credentials 对象
        Credentials credentials = new Credentials(username, Optional.ofNullable(password).orElse(""));
        PasswordSafe.getInstance().set(CREDENTIAL_ATTRIBUTES_PASSWORD, credentials);
    }

    public String getPassword() {
        return PasswordSafe.getInstance().getPassword(CREDENTIAL_ATTRIBUTES_PASSWORD);
    }

}
