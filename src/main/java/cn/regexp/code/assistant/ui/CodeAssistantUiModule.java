package cn.regexp.code.assistant.ui;

import cn.regexp.code.assistant.ui.issue.IssueListPanel;
import com.google.inject.AbstractModule;

/**
 * @author Regexpei
 * @date 2024/7/13 0:08
 * @description UI 模块
 * @since 1.0.0
 */
public class CodeAssistantUiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IssueListPanel.class).asEagerSingleton();
    }
}
