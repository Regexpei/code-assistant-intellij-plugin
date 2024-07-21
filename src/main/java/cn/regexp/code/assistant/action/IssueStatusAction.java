package cn.regexp.code.assistant.action;

import cn.regexp.code.assistant.entity.Issue;
import cn.regexp.code.assistant.enums.IssueStatusEnum;
import cn.regexp.code.assistant.util.ActionUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Regexpei
 * @date 2024/7/20 14:26
 * @description 问题状态操作
 * @since 1.0.0
 */
public class IssueStatusAction extends AnAction implements DumbAware {

    public IssueStatusAction(Integer status) {
        super(IssueStatusEnum.getDesc(status));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Optional<Issue> optional = ActionUtils.getSelected(e, Issue.class);
        if (optional.isPresent()) {
            Issue issue = optional.get();
            System.out.println(issue);
        }
    }
}
