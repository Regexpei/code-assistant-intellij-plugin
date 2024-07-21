package cn.regexp.code.assistant.action;

import cn.regexp.code.assistant.CodeAssistantModule;
import cn.regexp.code.assistant.entity.Issue;
import cn.regexp.code.assistant.util.ActionUtils;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @author Regexpei
 * @date 2024/7/20 13:43
 * @description
 * @since 1.0.0
 */
@SuppressWarnings("ComponentNotRegistered")
public class IssueStatusActionGroup extends ActionGroup implements DumbAware {

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setPopupGroup(true);
    }

    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
        Optional<Issue> optional = ActionUtils.getSelected(e, Issue.class);
        if (optional.isPresent()) {
            List<Integer> enableStatus = optional.get().getEnabledStatus();
            return createLabelsActions(enableStatus);
        }
        return new AnAction[0];
    }

    private AnAction[] createLabelsActions(List<Integer> statusList) {
        List<AnAction> labels = Lists.newArrayList();
        for (Integer status : statusList) {
            labels.add(new IssueStatusAction(status));
        }
        return labels.toArray(new AnAction[0]);
    }

    public static class Proxy extends IssueStatusActionGroup {
        private final IssueStatusActionGroup delegate;

        public Proxy() {
            delegate = CodeAssistantModule.getInstance(IssueStatusActionGroup.class);
        }

        @Override
        public void update(@NotNull AnActionEvent e) {
            delegate.update(e);
        }

        @NotNull
        @Override
        public AnAction @NotNull [] getChildren(@Nullable AnActionEvent anActionEvent) {
            return delegate.getChildren(anActionEvent);
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }
}
