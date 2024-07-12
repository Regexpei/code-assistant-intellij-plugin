package cn.regexp.code.assistant.ui.issue;

import com.google.inject.Inject;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;

/**
 * @author Regexpei
 * @date 2024/7/7 20:55
 * @description 问题工具窗口
 * @since 1.0.0
 */
public class IssueToolWindow {

    @Inject
    private IssueListPanel issueListPanel;

    public SimpleToolWindowPanel createToolWindowContent(Project project) {
        issueListPanel.setProject(project);
        issueListPanel.initData();

        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true, true);
        panel.setContent(issueListPanel);

        return panel;
    }
}
