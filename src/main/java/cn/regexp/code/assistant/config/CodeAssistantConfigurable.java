package cn.regexp.code.assistant.config;

import cn.regexp.code.assistant.CodeAssistantModule;
import cn.regexp.code.assistant.ui.CodeAssistantSettings;
import cn.regexp.code.assistant.ui.SettingsPanel;
import com.google.inject.Inject;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vcs.VcsConfigurableProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Regexpei
 * @date 2024/7/20 21:25
 * @description Code Assistant 配置（File -> Settings -> Other Settings）
 * @since 1.0.0
 */
public class CodeAssistantConfigurable implements SearchableConfigurable, VcsConfigurableProvider {

    private SettingsPanel settingsPane;
    @Inject
    private CodeAssistantSettings codeAssistantSettings;

    @Override
    public @NotNull @NonNls String getId() {
        return "settings.code.assistant";
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        // 展示的名称
        return "Code Assistant";
    }

    @Override
    public @Nullable JComponent createComponent() {
        if (settingsPane == null) {
            settingsPane = CodeAssistantModule.getInstance(SettingsPanel.class);
        }

        return settingsPane.getPanel();
    }

    // 是否发生修改
    @Override
    public boolean isModified() {
        return settingsPane != null
                && (!Comparing.equal(codeAssistantSettings.getHost(), settingsPane.getHost(), true)
                || !Comparing.equal(codeAssistantSettings.getUsername(), settingsPane.getUsername(), true)
                || settingsPane.isPasswordModified());
    }

    // 点击 Apply 按钮
    @Override
    public void apply() {
        if (settingsPane != null) {
            codeAssistantSettings.setHost(settingsPane.getHost());
            codeAssistantSettings.setUsername(settingsPane.getUsername());
            // 发生密码修改
            if (settingsPane.isPasswordModified()) {
                // 修改配置信息密码
                codeAssistantSettings.setPassword(settingsPane.getUsername(), settingsPane.getPassword());
                // 重置修改密码状态
                settingsPane.resetPasswordModification();
            }
        }
    }

    @Override
    public @Nullable Configurable getConfigurable(Project project) {
        return this;
    }

    @Override
    public void reset() {
        if (settingsPane == null) {
            return;
        }

        settingsPane.setHost(codeAssistantSettings.getHost());
        String username = codeAssistantSettings.getUsername();
        settingsPane.setUsername(username);
        settingsPane.setPassword(StringUtil.isEmptyOrSpaces(username) ? "" : codeAssistantSettings.getPassword());
        settingsPane.resetPasswordModification();
    }

    /**
     * CodeAssistantConfigurable 代理类
     */
    public static class Proxy extends CodeAssistantConfigurable {
        private final CodeAssistantConfigurable delegate;

        public Proxy() {
            delegate = CodeAssistantModule.getInstance(CodeAssistantConfigurable.class);
        }

        @Override
        @NotNull
        public String getDisplayName() {
            return delegate.getDisplayName();
        }

        @Override
        public JComponent createComponent() {
            return delegate.createComponent();
        }

        @Override
        public boolean isModified() {
            return delegate.isModified();
        }

        @Override
        public void apply() {
            delegate.apply();
        }

        @Override
        public void reset() {
            delegate.reset();
        }

        @Override
        @NotNull
        public String getId() {
            return delegate.getId();
        }

        @Nullable
        @Override
        public Configurable getConfigurable(Project project) {
            return delegate.getConfigurable(project);
        }
    }
}
