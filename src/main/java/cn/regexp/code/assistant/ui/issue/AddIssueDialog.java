package cn.regexp.code.assistant.ui.issue;

import cn.regexp.code.assistant.common.Constant;
import cn.regexp.code.assistant.entity.Result;
import cn.regexp.code.assistant.listener.RefreshListener;
import cn.regexp.code.assistant.service.CommonService;
import cn.regexp.code.assistant.service.IssueService;
import cn.regexp.code.assistant.util.FileUtils;
import cn.regexp.code.assistant.util.StringUtils;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static cn.regexp.code.assistant.common.Constant.COMMON_RESOURCE;

/**
 * @author Regexpei
 * @date 2024/7/7 16:31
 * @description 登记问题
 * @since 1.0.0
 */
public class AddIssueDialog extends DialogWrapper {

    public static final String ADD_ISSUE_ERROR_KEY = "message.addIssueError";

    private final Project project;
    private final Editor editor;
    private AddIssueForm addIssueForm;

    public AddIssueDialog(@Nullable Project project, Editor editor) {
        super(project);
        this.project = project;
        this.editor = editor;

        setTitle("登记问题");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        this.addIssueForm = new AddIssueForm(project);

        String filePath = FileUtils.getRelativePathByEditor(project, editor);
        int startPos = editor.getDocument().getLineNumber(editor.getSelectionModel().getSelectionStart());
        int endPos = editor.getDocument().getLineNumber(editor.getSelectionModel().getSelectionEnd());
        String selectedText = StringUtils.trimMultiLineStartBlank(editor.getSelectionModel().getSelectedText());
        this.addIssueForm.initFormData(filePath, selectedText, startPos + 1, endPos + 1);
        this.addIssueForm.setPreferredSize(new Dimension(800, 500));

        return addIssueForm.getMainPanel();
    }

    @Override
    protected void doOKAction() {
        // 在事件队列中异步处理添加问题的操作，以避免阻塞主线程
        CommonService.application.invokeLater(() -> {
            try {
                // 调用 IssueService 的实例方法添加问题
                Result<String> result = IssueService.getInstance().addIssue(addIssueForm.mapToIssueDTO());
                if (result.isSuccess()) {
                    // 如果添加成功，则关闭当前窗口，并刷新问题列表
                    this.close(1);
                    project.getMessageBus().syncPublisher(RefreshListener.ADD_ISSUE_TOPIC).refresh();
                } else {
                    // 如果添加失败，显示错误对话框
                    Messages.showErrorDialog(result.getMsg(), Constant.ERROR);
                }
            } catch (Exception e) {
                // 捕获异常情况，显示通用错误对话框，并重新抛出异常以便进一步处理
                Messages.showErrorDialog(COMMON_RESOURCE.getString(ADD_ISSUE_ERROR_KEY), Constant.ERROR);
                throw e;
            }
        });
    }

}
