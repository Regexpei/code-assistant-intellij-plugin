package cn.regexp.code.assistant.factory;

import cn.regexp.code.assistant.CodeAssistantModule;
import cn.regexp.code.assistant.ui.issue.IssueToolWindow;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Regexpei
 * @date 2024/7/7 20:56
 * @description 问题工具窗口工厂
 * @since 1.0.0
 */
public class CodeAssistantToolWindowFactory implements ToolWindowFactory, DumbAware {

    private static final String TAB_ISSUE_LIST = "Issue";
    private IssueToolWindow issueToolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        this.issueToolWindow = CodeAssistantModule.getInstance(IssueToolWindow.class);
        addIssueList(project, contentManager);
    }

    private void addIssueList(@NotNull Project project, ContentManager contentManager) {
        SimpleToolWindowPanel toolWindowContent = issueToolWindow.createIssueToolWindowContent(project);

        Content content = contentManager.getFactory().createContent(toolWindowContent, TAB_ISSUE_LIST, false);
        content.setCloseable(false);
        contentManager.addContent(content);
    }

}
