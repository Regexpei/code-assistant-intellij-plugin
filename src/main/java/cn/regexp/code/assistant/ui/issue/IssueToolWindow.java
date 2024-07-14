package cn.regexp.code.assistant.ui.issue;

import com.google.inject.Inject;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.OnePixelSplitter;

/**
 * @author Regexpei
 * @date 2024/7/7 20:55
 * @description 问题工具窗口
 * @since 1.0.0
 */
public class IssueToolWindow {

    @Inject
    private IssueListPanel issueListPanel;

    public SimpleToolWindowPanel createIssueToolWindowContent(Project project) {
        issueListPanel.setProject(project);
        // 添加刷新监听器
        issueListPanel.addRefreshListener();
        issueListPanel.initData();

        IssueDetailPanel issueDetailPanel = new IssueDetailPanel();
        issueListPanel.addListSelectionListener(issueDetailPanel::setData);

        JBSplitter horizontalSplitter = new OnePixelSplitter(false, 0.7f);
        horizontalSplitter.setFirstComponent(issueListPanel);
        horizontalSplitter.setSecondComponent(issueDetailPanel.getPanel());

        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true, true);
        panel.setContent(horizontalSplitter);
        return panel;
    }

}
