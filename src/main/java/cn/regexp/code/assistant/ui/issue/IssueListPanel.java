package cn.regexp.code.assistant.ui.issue;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.regexp.code.assistant.entity.Issue;
import cn.regexp.code.assistant.enums.IssueLevelEnum;
import cn.regexp.code.assistant.enums.IssueStatusEnum;
import cn.regexp.code.assistant.enums.IssueTypeEnum;
import cn.regexp.code.assistant.enums.PriorityEnum;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.TableView;
import com.intellij.util.text.DateFormatUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import com.intellij.util.ui.UIUtil;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Setter
    private Project project;

    /**
     * 列英文名和列中文名映射
     */
    private static final Map<String, String> COLUMNS_MAP = new LinkedHashMap<>();

    static {
        COLUMNS_MAP.put("issueNo", "#");
        COLUMNS_MAP.put("projectName", "项目名称");
        COLUMNS_MAP.put("branchName", "分支名称");
        COLUMNS_MAP.put("assignee", "责任人");
        COLUMNS_MAP.put("marker", "登记人");
        COLUMNS_MAP.put("issueType", "问题类型");
        COLUMNS_MAP.put("issueLevel", "问题级别");
        COLUMNS_MAP.put("issueStatus", "问题状态");
        COLUMNS_MAP.put("priority", "优先级");
        COLUMNS_MAP.put("createdTime", "创建时间");
    }

    // -------------------------------------------------- 公共方法

    public IssueListPanel() {
        setLayout(new BorderLayout());
        this.issueList = Lists.newArrayList();
        this.issueTableView = new TableView<>();

        // 设置表格为斑马纹样式，即交替行颜色不同，以便于阅读和区分。
        this.issueTableView.setStriped(true);
        this.scrollPane = ScrollPaneFactory.createScrollPane(issueTableView);

        // 将滚动面板添加到面板中
        add(this.scrollPane);
    }

    public void initData() {
        this.issueList.clear();

        // 查询数据
        String data = FileUtil.readString(
                "E:\\IdeaProjects\\code-assistant-intellij-plugin\\doc\\mock\\MockIssueData.json",
                Charset.defaultCharset());
        this.issueList.addAll(JSON.parseArray(data, Issue.class));

        issueTableView.setModelAndUpdateColumns(
                new ListTableModel<>(generateColumnsInfo(this.issueList), this.issueList, 0));
        issueTableView.repaint();
    }


    @SuppressWarnings("rawtypes")
    public ColumnInfo[] generateColumnsInfo(List<Issue> issueList) {
        if (CollectionUtil.isEmpty(issueList)) {
            return new ColumnInfo[0];
        }

        Map<String, ItemAndWidth> columnWidthMap = new LinkedHashMap<>();
        for (int i = 0; i < issueList.size(); ) {
            Issue issue = issueList.get(i);
            issue.setIssueNo(String.valueOf(++i));

            for (String columnName : COLUMNS_MAP.keySet()) {
                putColumnWidths(columnWidthMap, columnName, getValue(columnName, issue));
            }
        }

        return columnWidthMap.entrySet().stream().map(entry -> createColumnInfo(entry.getKey(), entry.getValue()))
                .toArray(ColumnInfo[]::new);
    }

    // -------------------------------------------------- 私有方法

    private ItemAndWidth getMax(ItemAndWidth current, String candidate) {
        if (candidate == null) {
            return current;
        }
        int width = issueTableView.getFontMetrics(issueTableView.getFont()).stringWidth(candidate);
        if (width > current.width) {
            return new ItemAndWidth(candidate, width);
        }
        return current;
    }


    private void putColumnWidths(Map<String, ItemAndWidth> columnWidths, String columnName, String value) {
        // 不存在则创建，存在则不处理
        columnWidths.computeIfAbsent(columnName, (key) -> new ItemAndWidth("", 0));
        // 存在则更新，不存在则不处理
        columnWidths.computeIfPresent(columnName, (key, itemAndWidth) -> getMax(itemAndWidth, value));
    }

    private ColumnInfo<Issue, String> createColumnInfo(String columnName, ItemAndWidth itemAndWidth) {
        return new IssueColumnInfo(COLUMNS_MAP.get(columnName), itemAndWidth.item()) {
            @Nullable
            @Override
            public String valueOf(Issue issue) {
                return getValue(columnName, issue);
            }
        };
    }

    // Java 14 switch 支持箭头标签和 yield 语句
    private String getValue(String columnName, Issue issue) {
        return switch (columnName) {
            case "issueNo" -> issue.getIssueNo();
            case "projectName" -> issue.getProjectName();
            case "branchName" -> issue.getBranchName();
            case "assignee" -> issue.getAssignee();
            case "marker" -> issue.getMarker();
            case "issueType" -> IssueTypeEnum.getDesc(issue.getIssueType());
            case "issueLevel" -> IssueLevelEnum.getDesc(issue.getIssueLevel());
            case "issueStatus" -> IssueStatusEnum.getDesc(issue.getIssueStatus());
            case "priority" -> PriorityEnum.getDesc(issue.getPriority());
            case "createdTime" -> {
                if (issue.getCreatedTime() == null) {
                    yield "";
                }
                yield DateFormatUtil.formatPrettyDateTime(issue.getCreatedTime());
            }
            default -> "";
        };
    }

    // -------------------------------------------------- 内部类

    // record（Java 14 引入的新特性，用于简化具有简单构造器和少量字段的类的定义）
    private record ItemAndWidth(String item, int width) {}

    private abstract static class IssueColumnInfo extends ColumnInfo<Issue, String> {

        @NotNull
        private final String maxString;

        IssueColumnInfo(@NotNull @NlsContexts.ColumnName String name, @NotNull String maxString) {
            super(name);
            this.maxString = maxString;
        }

        @Override
        public String getMaxStringValue() {
            return maxString;
        }

        @Override
        public int getAdditionalWidth() {
            return UIUtil.DEFAULT_HGAP;
        }
    }
}
