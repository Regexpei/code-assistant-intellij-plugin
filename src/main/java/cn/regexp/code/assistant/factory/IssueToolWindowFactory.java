package cn.regexp.code.assistant.factory;

import cn.regexp.code.assistant.CodeAssistantModule;
import cn.regexp.code.assistant.ui.issue.IssueToolWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author Regexpei
 * @date 2024/7/7 20:56
 * @description 问题工具窗口工厂
 * @since 1.0.0
 */
public class IssueToolWindowFactory implements ToolWindowFactory {

    private static final String DISPLAY_NAME = "Issue List";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        IssueToolWindow issueToolWindow = CodeAssistantModule.getInstance(IssueToolWindow.class);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(issueToolWindow, DISPLAY_NAME, false);
        content.setCloseable(false);
        toolWindow.getContentManager().addContent(content);
    }
}
