package cn.regexp.code.assistant.action;

import cn.regexp.code.assistant.ui.issue.AddIssueDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.Messages;

/**
 * @author Regexpei
 * @date 2024/7/7 15:49
 * @description 标记问题操作
 * @since 1.0.0
 */
public class MarkIssueAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(DataKey.create("editor"));
        if (editor == null) {
            return;
        }

        SelectionModel selectionModel = editor.getSelectionModel();
        if (!selectionModel.hasSelection()) {
            // noinspection DialogTitleCapitalization
            Messages.showErrorDialog("请先选中文件内容再点击！", "Tip");
        }

        AddIssueDialog dialog = new AddIssueDialog(e.getProject(), editor);
        dialog.show();
    }
}
