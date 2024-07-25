package cn.regexp.code.assistant.ui.issue;

import cn.regexp.code.assistant.entity.Issue;
import cn.regexp.code.assistant.entity.User;
import cn.regexp.code.assistant.enums.IssueLevelEnum;
import cn.regexp.code.assistant.enums.IssueStatusEnum;
import cn.regexp.code.assistant.enums.IssueTypeEnum;
import cn.regexp.code.assistant.enums.PriorityEnum;
import cn.regexp.code.assistant.service.UserService;
import cn.regexp.code.assistant.ui.SimpleComboBoxItem;
import cn.regexp.code.assistant.util.GitRepositoryUtils;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.JBUI;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

/**
 * @author Regexpei
 * @date 2024/7/7 15:59
 * @description 问题表单
 * @since 1.0.0
 */
@Slf4j
public class AddIssueForm extends JPanel {
    private final Project project;
    @Getter
    private JPanel mainPanel;
    private JTextField currentBranchField;
    private JComboBox<SimpleComboBoxItem> assigneeBox;
    private JComboBox<SimpleComboBoxItem> markerBox;
    private JTextField startPosField;
    private JTextField endPosField;
    private JComboBox<SimpleComboBoxItem> issueTypeBox;
    private JComboBox<SimpleComboBoxItem> issueLevelBox;
    private JComboBox<SimpleComboBoxItem> issueStatusBox;
    private JTextField filePathField;
    private JTextArea codeSegmentArea;
    private JTextArea issueDetailArea;
    private JTextArea suggestionArea;
    private JComboBox<SimpleComboBoxItem> priorityBox;


    public AddIssueForm(Project project) {
        super();
        this.project = project;
        initUI();
        initComboBoxData();
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    private void initUI() {
        this.codeSegmentArea.setMargin(JBUI.insets(5));
        this.issueDetailArea.setMargin(JBUI.insets(5));
        this.suggestionArea.setMargin(JBUI.insets(5));
    }

    public void initFormData(String filePath, String selectedText, int startPos, int endPos) {
        this.filePathField.setText(filePath);
        this.startPosField.setText(String.valueOf(startPos));
        this.endPosField.setText(String.valueOf(endPos));
        this.codeSegmentArea.setText(selectedText);
        this.currentBranchField.setText(GitRepositoryUtils.getCurrentBranchName(project));
    }

    private void initComboBoxData() {
        for (IssueLevelEnum value : IssueLevelEnum.values()) {
            SimpleComboBoxItem item = new SimpleComboBoxItem(String.valueOf(value.getCode()), value.getDesc());
            this.issueLevelBox.addItem(item);
        }

        for (IssueStatusEnum value : IssueStatusEnum.values()) {
            SimpleComboBoxItem item = new SimpleComboBoxItem(String.valueOf(value.getCode()), value.getDesc());
            this.issueStatusBox.addItem(item);
        }

        for (IssueTypeEnum value : IssueTypeEnum.values()) {
            SimpleComboBoxItem item = new SimpleComboBoxItem(String.valueOf(value.getCode()), value.getDesc());
            this.issueTypeBox.addItem(item);
        }

        for (PriorityEnum value : PriorityEnum.values()) {
            SimpleComboBoxItem item = new SimpleComboBoxItem(String.valueOf(value.getCode()), value.getDesc());
            this.priorityBox.addItem(item);
        }

        List<User> pluginUserList = UserService.getInstance().listPluginUser(project);
        for (User user : pluginUserList) {
            SimpleComboBoxItem item = new SimpleComboBoxItem(user.getUsername(), user.getName());
            this.assigneeBox.addItem(item);
        }

        User currentUser = UserService.getInstance().getCurrentUser(project);
        SimpleComboBoxItem item = new SimpleComboBoxItem(currentUser.getUsername(), currentUser.getName());
        this.markerBox.addItem(item);
        this.markerBox.setSelectedItem(item);
    }

    public Issue mapToIssueDTO() {
        Issue issue = new Issue();
        issue.setGitUrl(GitRepositoryUtils.getGitUrl(project));
        issue.setProjectName(project.getName());
        issue.setBranchName(this.currentBranchField.getText());
        issue.setAssignee(getCode(this.assigneeBox));
        issue.setMarker(getCode(this.markerBox));
        issue.setIssueType(Integer.valueOf(Objects.requireNonNull(getCode(this.issueTypeBox))));
        issue.setIssueLevel(Integer.valueOf(Objects.requireNonNull(getCode(this.issueLevelBox))));
        issue.setIssueStatus(Integer.valueOf(Objects.requireNonNull(getCode(this.issueStatusBox))));
        issue.setPriority(Integer.valueOf(Objects.requireNonNull(getCode(this.priorityBox))));
        issue.setStartPos(Integer.parseInt(this.startPosField.getText()));
        issue.setEndPos(Integer.parseInt(this.endPosField.getText()));
        issue.setFilePath(this.filePathField.getText());
        issue.setCodeSegment(this.codeSegmentArea.getText());
        issue.setIssueDetail(this.issueDetailArea.getText());
        issue.setSuggestion(this.suggestionArea.getText());
        return issue;
    }

    private String getCode(JComboBox<SimpleComboBoxItem> jComboBox) {
        SimpleComboBoxItem item = (SimpleComboBoxItem) jComboBox.getSelectedItem();
        if (item == null) {
            return null;
        }
        return item.getCode();
    }

}
