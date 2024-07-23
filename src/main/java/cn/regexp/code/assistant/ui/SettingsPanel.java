package cn.regexp.code.assistant.ui;

import cn.regexp.code.assistant.entity.Result;
import cn.regexp.code.assistant.util.HttpUtils;
import com.google.inject.Inject;
import com.intellij.openapi.util.text.StringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Regexpei
 * @date 2024/7/20 20:45
 * @description 设置页面
 * @since 1.0.0
 */
@Slf4j
public class SettingsPanel {

    private static final String HTML_TEMPLATE_ERROR = "<span style=\"color: red;\">%s</span>";
    private static final String HTML_TEMPLATE_INFO = "<span style=\"color: green;\">%s</span>";

    @Getter
    private JPanel panel;
    private JTextField hostField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton testButton;
    private JTextPane resultPane;
    @Getter
    private boolean passwordModified = false;
    @Inject
    private CodeAssistantSettings codeAssistantSettings;

    public SettingsPanel() {
        // 添加监听器，当发生插入、删除、修改时，将 passwordModified 设置为 true
        passwordField.getDocument().addDocumentListener(new PasswordDocumentListener());

        testButton.addActionListener(e -> {
            if (StringUtil.isEmpty(this.hostField.getText())) {
                resultPane.setText(String.format(HTML_TEMPLATE_ERROR, "Web-URL 不能为空"));
                return;
            }
            if (StringUtil.isEmpty(this.usernameField.getText())) {
                resultPane.setText(String.format(HTML_TEMPLATE_ERROR, "账号不能为空！"));
                return;
            }
            if (StringUtil.isEmpty(getPassword())) {
                resultPane.setText(String.format(HTML_TEMPLATE_ERROR, "密码不能为空！"));
                return;
            }

            String host = hostField.getText();
            String username = usernameField.getText();
            // 密码变更了则以最新的为准，否则获取配置中的
            String password = isPasswordModified() ? getPassword() : codeAssistantSettings.getPassword();

            Map<String, String> authInfoMap = new HashMap<>();
            authInfoMap.put("username", username);
            authInfoMap.put("password", password);

            String url = host + "/plugin/user/login";
            try {
                Result<String> result = HttpUtils.post(url, authInfoMap);
                if (result.isSuccess()) {
                    // 登录成功
                    resultPane.setText(String.format(HTML_TEMPLATE_INFO, "Login successfully!"));
                    codeAssistantSettings.setToken(result.getData());
                } else {
                    resultPane.setText(String.format(HTML_TEMPLATE_ERROR, result.getMsg()));
                }
            } catch (Exception ex) {
                log.error("Login failed: {}", ex.getMessage(), ex);
                resultPane.setText(String.format(HTML_TEMPLATE_ERROR, "登录异常，请稍后再试！"));
            }
        });
    }

    public void resetPasswordModification() {
        passwordModified = false;
    }

    private class PasswordDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            passwordModified = true;
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            passwordModified = true;
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            passwordModified = true;
        }
    }

    public String getHost() {
        return hostField.getText();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void setHost(String host) {
        hostField.setText(host);
    }

    public void setUsername(String username) {
        usernameField.setText(username);
    }

    public void setPassword(String password) {
        passwordField.setText(StringUtil.isEmpty(password) ? "" : password);
    }
}
