package cn.regexp.code.assistant.ui.issue;

import cn.hutool.http.HtmlUtil;
import cn.regexp.code.assistant.entity.Issue;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import com.intellij.vcsUtil.UIVcsUtil;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.Boolean.TRUE;
import static javax.swing.JEditorPane.HONOR_DISPLAY_PROPERTIES;

/**
 * @author Regexpei
 * @date 2024/7/14 0:07
 * @description
 * @since 1.0.0
 */
public class IssueDetailPanel {

    private static final String NOTHING_SELECTED = "nothingSelected";
    private static final String LOADING = "loading";
    private static final String DATA = "data";

    @Getter
    private final JPanel panel;
    private final PresentationData presentationData;
    private final JEditorPane jEditorPane;

    public IssueDetailPanel() {
        // 设定面板布局为卡片布局
        panel = new JPanel(new CardLayout());
        // 添加一个表示“无内容选择”的错误面板，当没有内容被选择时，将显示该面板
        panel.add(UIVcsUtil.errorPanel("Nothing selected", false), NOTHING_SELECTED);
        // 添加一个表示“加载中”的错误面板，当正在加载时，将显示该面板
        panel.add(UIVcsUtil.errorPanel("Loading...", false), LOADING);

        presentationData = new PresentationData();

        // 创建一个 JEditorPane 实例，用于显示 HTML 内容
        jEditorPane = new JEditorPane(UIUtil.HTML_MIME, "");
        // 设置首选尺寸
        jEditorPane.setPreferredSize(new Dimension(150, 100));
        // 禁止编辑
        jEditorPane.setEditable(false);
        // 设置不透明
        jEditorPane.setOpaque(false);
        // 设置客户端属性，以确保 JEditorPane 正确处理 HTML 中的显示属性（如颜色、字体等）
        jEditorPane.putClientProperty(HONOR_DISPLAY_PROPERTIES, TRUE);

        JPanel wrapper = new JPanel(new BorderLayout());
        JBScrollPane tableScroll = new JBScrollPane(jEditorPane);
        tableScroll.setBorder(null);
        wrapper.add(tableScroll, SwingConstants.CENTER);

        panel.add(wrapper, DATA);
        // 展示“无内容选择”面板
        ((CardLayout) panel.getLayout()).show(panel, NOTHING_SELECTED);
    }

    public void setData(Issue issue) {
        // 展示“加载中”面板
        ((CardLayout) panel.getLayout()).show(panel, LOADING);
        if (issue == null) {
            ((CardLayout) panel.getLayout()).show(panel, NOTHING_SELECTED);
            return;
        }
        // 初始化面板数据
        presentationData.setIssue(issue);
        if (presentationData.isReady()) {
            jEditorPane.setText(presentationData.getText());
            // 重新计算面板合适布局
            panel.revalidate();
            // 刷新面板
            panel.repaint();
            // 设置光标位置到编辑器面板的起始位置
            jEditorPane.setCaretPosition(0);
        }

        // 展示“数据”面板
        ((CardLayout) panel.getLayout()).show(panel, DATA);
    }

    private static class PresentationData {

        /**
         * HTML模板
         */
        public static final String HTML_TEMPLATE = """
                <div style=margin:5px;font-size:10px;color:#000000;>
                    <span style=font-weight:bold;>${key}</span>
                    <br/>
                    <pre>${value}</pre>
                </div>""";

        private String startPattern;
        private static final String END_PATTERN = "</table></body></html>";

        public void setIssue(Issue issue) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> map = new LinkedHashMap<>();
            map.put("文件路径", issue.getFilePath());
            map.put("代码详情", issue.getCodeSegment());
            map.put("问题描述", issue.getIssueDetail());
            map.put("修复建议", issue.getSuggestion());

            map.forEach((key, value) -> sb.append(
                    HTML_TEMPLATE.replace("${key}", key).replace("${value}", HtmlUtil.escape(value))));

            startPattern = sb.toString();
        }

        public boolean isReady() {
            return startPattern != null;
        }

        public String getText() {
            return startPattern + END_PATTERN;
        }

    }
}
