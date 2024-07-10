package cn.regexp.code.assistant.ui.issue;

import cn.regexp.code.assistant.util.FileUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author Regexpei
 * @date 2024/7/7 16:31
 * @description 登记问题
 * @since 1.0.0
 */
public class AddIssueDetailDialog extends DialogWrapper {

    private final Project project;
    private final Editor editor;

    private IssueDetailForm issueDetailForm;

    public AddIssueDetailDialog(@Nullable Project project, Editor editor) {
        super(project);
        this.project = project;
        this.editor = editor;

        setTitle("登记问题");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        this.issueDetailForm = new IssueDetailForm(project);

        String filePath = FileUtils.getRelativePathByEditor(project, editor);
        int startPos = editor.getDocument().getLineNumber(editor.getSelectionModel().getSelectionStart());
        int endPos = editor.getDocument().getLineNumber(editor.getSelectionModel().getSelectionEnd());
        String selectedText = editor.getSelectionModel().getSelectedText();
        this.issueDetailForm.initFormData(filePath, selectedText, startPos + 1, endPos + 1);
        this.issueDetailForm.setPreferredSize(new Dimension(800, 500));

        return issueDetailForm.getMainPanel();
    }

    @Override
    protected void doOKAction() {
        System.out.println("点击了ok");
        System.out.println(JSON.toJSONString(issueDetailForm.mapToIssueDTO(), JSONWriter.Feature.PrettyFormat));
    }

}
