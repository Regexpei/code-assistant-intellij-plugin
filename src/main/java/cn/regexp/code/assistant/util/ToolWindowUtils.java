package cn.regexp.code.assistant.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;

/**
 * @author Regexpei
 * @date 2024/7/13 14:04
 * @description 窗口工具类
 * @since 1.0.0
 */
public class ToolWindowUtils {

    private ToolWindowUtils() {
    }

    /**
     * 跳转到指定 Tab 页
     *
     * @param project      当前项目
     * @param toolWindowId 窗口 ID
     * @param index        tab 索引下标
     */
    public static void switchTab(Project project, String toolWindowId, int index) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(toolWindowId);
        if (toolWindow == null) {
            return;
        }

        Content content = toolWindow.getContentManager().getContent(index);
        if (content == null) {
            return;
        }
        toolWindow.getContentManager().setSelectedContent(content);
    }


}
