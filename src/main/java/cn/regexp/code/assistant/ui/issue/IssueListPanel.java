package cn.regexp.code.assistant.ui.issue;

import cn.regexp.code.assistant.entity.Issue;
import cn.regexp.code.assistant.enums.IssueLevelEnum;
import cn.regexp.code.assistant.enums.IssueStatusEnum;
import cn.regexp.code.assistant.enums.IssueTypeEnum;
import cn.regexp.code.assistant.enums.PriorityEnum;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.TableView;
import com.intellij.util.text.DateFormatUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Regexpei
 * @date 2024/7/7 20:59
 * @description 问题列表面板
 * @since 1.0.0
 */
public class IssueListPanel extends JPanel {
    /**
     * 表格视图
     */
    private final TableView<Issue> issueTableView;

    /**
     * 滚动条
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

    public void initData() {
        this.issueList.clear();

        // 查询数据
        String data = "{\n" + "\t\"id\": 1,\n" + "\t\"branchName\": \"master\",\n" + "\t\"codeSegment\": " +
                "\"eating-mgrsite\",\n" + "\t\"createdTime\": \"2024-07-11 23:48:48.588\",\n" + "\t\"endPos\": 13,\n" + "\t\"filePath\": \"/pom.xml\",\n" + "\t\"gitUrl\": \"https://gitee.com/regexpei/eating-time.git\",\n" + "\t\"issueDetail\": \"1231\",\n" + "\t\"issueLevel\": 1,\n" + "\t\"issueStatus\": 1,\n" + "\t\"issueType\": 1,\n" + "\t\"priority\": 1,\n" + "\t\"projectName\": \"eating-time\",\n" + "\t\"startPos\": 13,\n" + "\t\"suggestion\": \"1221\",\n" + "\t\"updatedTime\": \"2024-07-11 23:48:48.588\"\n" + "}";
        Issue issue = JSON.parseObject(data, Issue.class);
        this.issueList.add(issue);

        issueTableView.setModelAndUpdateColumns(
                new ListTableModel<>(generateColumnsInfo(this.issueList), this.issueList, 0));
        issueTableView.repaint();
    }


    public ColumnInfo[] generateColumnsInfo(List<Issue> issueList) {
        ItemAndWidth number = new ItemAndWidth("", 0);
        ItemAndWidth projectName = new ItemAndWidth("", 0);
        ItemAndWidth branchName = new ItemAndWidth("", 0);
        ItemAndWidth assignee = new ItemAndWidth("", 0);
        ItemAndWidth marker = new ItemAndWidth("", 0);
        ItemAndWidth issueType = new ItemAndWidth("", 0);
        ItemAndWidth issueLevel = new ItemAndWidth("", 0);
        ItemAndWidth issueStatus = new ItemAndWidth("", 0);
        ItemAndWidth priority = new ItemAndWidth("", 0);
        ItemAndWidth createTime = new ItemAndWidth("", 0);

        for (Issue issue : issueList) {
            number = getMax(number, String.valueOf(issue.getId()));
            projectName = getMax(projectName, issue.getProjectName());
            branchName = getMax(branchName, issue.getBranchName());
            assignee = getMax(assignee, issue.getAssignee());
            marker = getMax(marker, issue.getAssignee());
            issueType = getMax(issueType, IssueTypeEnum.getDesc(issue.getIssueType()));
            issueLevel = getMax(issueLevel, IssueLevelEnum.getDesc(issue.getIssueLevel()));
            issueStatus = getMax(issueStatus, IssueStatusEnum.getDesc(issue.getIssueStatus()));
            priority = getMax(priority, PriorityEnum.getDesc(issue.getPriority()));
            createTime = getMax(createTime, DateFormatUtil.formatPrettyDateTime(issue.getCreatedTime()));
        }

        return new ColumnInfo[]{new IssueColumnInfo("#", number.myItem) {
            @Override
            public @NotNull String valueOf(Issue issue) {
                return issue.getId().toString();
            }
        }, new IssueColumnInfo("项目名称", projectName.myItem) {
            @Override
            public @Nullable String valueOf(Issue issue) {
                return issue.getProjectName();
            }
        }, new IssueColumnInfo("分支名称", branchName.myItem) {
            @Override
            public @Nullable String valueOf(Issue issue) {
                return issue.getBranchName();
            }
        }, new IssueColumnInfo("责任人", assignee.myItem) {
            @Override
            public @Nullable String valueOf(Issue issue) {
                return issue.getAssignee();
            }
        }, new IssueColumnInfo("登记人", marker.myItem) {
            @Override
            public @Nullable String valueOf(Issue issue) {
                return issue.getMarker();
            }
        }, new IssueColumnInfo("问题类型", issueType.myItem) {
            @Override
            public @Nullable String valueOf(Issue issue) {
                return IssueTypeEnum.getDesc(issue.getIssueType());
            }
        }, new IssueColumnInfo("问题级别", issueLevel.myItem) {
            @Override
            public @Nullable String valueOf(Issue issue) {
                return IssueLevelEnum.getDesc(issue.getIssueLevel());
            }
        }, new IssueColumnInfo("问题状态", issueStatus.myItem) {
            @Override
            public @Nullable String valueOf(Issue issue) {
                return IssueStatusEnum.getDesc(issue.getIssueLevel());
            }
        }, new IssueColumnInfo("优先级", priority.myItem) {
            @Override
            public @Nullable String valueOf(Issue issue) {
                return PriorityEnum.getDesc(issue.getPriority());
            }
        }, new IssueColumnInfo("创建时间", createTime.myItem) {
            @Override
            public @NotNull String valueOf(Issue issue) {
                return DateFormatUtil.formatPrettyDateTime(issue.getCreatedTime());
            }
        }};
    }

    private ItemAndWidth getMax(ItemAndWidth current, String candidate) {
        if (candidate == null) {
            return current;
        }
        int width = issueTableView.getFontMetrics(issueTableView.getFont()).stringWidth(candidate);
        if (width > current.myWidth) {
            return new ItemAndWidth(candidate, width);
        }
        return current;
    }

    private static final class ItemAndWidth {
        private final String myItem;
        private final int myWidth;

        private ItemAndWidth(String item, int width) {
            myItem = item;
            myWidth = width;
        }
    }

    private abstract static class IssueColumnInfo extends ColumnInfo<Issue, String> {

        @NotNull
        private final String myMaxString;

        IssueColumnInfo(@NotNull @NlsContexts.ColumnName String name, @NotNull String maxString) {
            super(name);
            myMaxString = maxString;
        }

        @Override
        public String getMaxStringValue() {
            return myMaxString;
        }

        @Override
        public int getAdditionalWidth() {
            return UIUtil.DEFAULT_HGAP;
        }
    }
}
