package cn.regexp.code.assistant.ui.issue;

import cn.regexp.code.assistant.entity.Issue;
import com.google.common.collect.Lists;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.TableView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Regexpei
 * @date 2024/7/7 20:59
 * @description 问题列表面板
 */
public class IssueListPanel extends JPanel {
    /**
     * 表格视图
     */
    private final TableView<Issue> issueTableView;

    /**
     * 滚动条面板
     */
    private final JScrollPane scrollPane;

    private final List<Issue> issueList;


    public IssueListPanel() {
        setLayout(new BorderLayout());
        this.issueList = Lists.newArrayList();
        this.issueTableView = new TableView<>();
        this.scrollPane = ScrollPaneFactory.createScrollPane(issueTableView);
        // 将滚动面板添加到面板中
        add(this.scrollPane);
    }
}
