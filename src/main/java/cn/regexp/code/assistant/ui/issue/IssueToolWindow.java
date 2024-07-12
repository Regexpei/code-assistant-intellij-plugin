package cn.regexp.code.assistant.ui.issue;

import com.intellij.openapi.ui.SimpleToolWindowPanel;

/**
 * @author Regexpei
 * @date 2024/7/7 20:55
 * @description 问题工具窗口
 * @since 1.0.0
 */
public class IssueToolWindow extends SimpleToolWindowPanel {

    public IssueToolWindow() {
        super(false, false);
        IssueListPanel issueListPanel = new IssueListPanel();
        issueListPanel.initData();
        setContent(issueListPanel);
    }


}
