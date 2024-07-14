package cn.regexp.code.assistant.ui.issue;

import cn.regexp.code.assistant.common.persistence.JsonPersistence;
import cn.regexp.code.assistant.entity.Issue;
import cn.regexp.code.assistant.listener.RefreshListener;
import cn.regexp.code.assistant.util.FileUtils;
import cn.regexp.code.assistant.util.StringUtils;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @author Regexpei
 * @date 2024/7/7 16:31
 * @description 登记问题
 * @since 1.0.0
 */
public class AddIssueDialog extends DialogWrapper {

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
        JsonPersistence<Issue> persistence = JsonPersistence.getInstance(project, Issue.class);
        try {
            persistence.save(addIssueForm.mapToIssueDTO());
            // 关闭窗口
            this.close(1);
            // 刷新问题列表
            project.getMessageBus().syncPublisher(RefreshListener.ADD_ISSUE_TOPIC).refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
