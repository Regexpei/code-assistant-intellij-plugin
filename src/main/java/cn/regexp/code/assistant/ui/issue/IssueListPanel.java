package cn.regexp.code.assistant.ui.issue;

import cn.hutool.core.collection.CollectionUtil;
import cn.regexp.code.assistant.common.persistence.JsonPersistence;
import cn.regexp.code.assistant.entity.Issue;
import cn.regexp.code.assistant.enums.IssueLevelEnum;
import cn.regexp.code.assistant.enums.IssueStatusEnum;
import cn.regexp.code.assistant.enums.IssueTypeEnum;
import cn.regexp.code.assistant.enums.PriorityEnum;
import cn.regexp.code.assistant.listener.RefreshListener;
import cn.regexp.code.assistant.util.FileUtils;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.TableView;
import com.intellij.util.Consumer;
import com.intellij.util.text.DateFormatUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import com.intellij.util.ui.UIUtil;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
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

    public static final String POPUP_MENU_GROUP_ID = "CodeAssistant.Issue.ListPopup";

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
        // 设置表格选择模式为单选
        this.issueTableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 双击事件监听
        this.issueTableView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Issue issue = issueTableView.getSelectedObject();
                    if (issue == null) {
                        return;
                    }
                    // 双击跳转到对应文件
                    FileUtils.jumpToFile(project, issue.getFilePath());
                }
            }
        });
        this.scrollPane = ScrollPaneFactory.createScrollPane(issueTableView);

        // 安装右键菜单，ActionPlaces.UNKNOWN 表示动作的显示位置未被明确指
        PopupHandler.installPopupMenu(issueTableView, POPUP_MENU_GROUP_ID, ActionPlaces.UNKNOWN);

        // 将滚动面板添加到面板中
        add(this.scrollPane);
    }

    public void initData() {
        this.issueList.clear();

        // 查询数据（后续修改为调用接口获取）
        this.issueList.addAll(JsonPersistence.getInstance(project, Issue.class).load());
        // 按照创建时间降序排序
        this.issueList.sort(Comparator.comparing(Issue::getCreatedTime).reversed());

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

        return columnWidthMap.keySet().stream().map(this::createColumnInfo)
                .toArray(ColumnInfo[]::new);
    }


    /**
     * 添加列表选择监听器，当用户选中时，将更改信息传递给监听器。
     *
     * @param listener 监听器
     */
    public void addListSelectionListener(final @NotNull Consumer<Issue> listener) {
        issueTableView.getSelectionModel()
                .addListSelectionListener(e -> listener.consume(issueTableView.getSelectedObject()));
    }

    /**
     * 添加刷新监听器，当用户添加问题时，将更改信息传递给监听器
     */
    public void addRefreshListener() {
        this.project.getMessageBus().connect()
                .subscribe(RefreshListener.ADD_ISSUE_TOPIC, (RefreshListener) this::initData);
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

    private ColumnInfo<Issue, String> createColumnInfo(String columnName) {
        return new IssueColumnInfo(COLUMNS_MAP.get(columnName)) {
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

        IssueColumnInfo(@NotNull @NlsContexts.ColumnName String name) {
            super(name);
        }

        @Override
        public int getAdditionalWidth() {
            return UIUtil.DEFAULT_HGAP;
        }
    }
}
