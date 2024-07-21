package cn.regexp.code.assistant.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.ui.table.TableView;

import java.awt.*;
import java.util.Optional;

/**
 * @author Regexpei
 * @date 2024/7/20 13:51
 * @description 操作工具类
 * @since 1.0.0
 */
public class ActionUtils {

    private ActionUtils() {
    }

    /**
     * 获取列表中被选中的对象
     *
     * @param anActionEvent 操作事件
     * @param tClass        被选中的对象类型 Class
     * @param <T>           被选中对象类型
     * @return 被选中对象容器
     */
    public static <T> Optional<T> getSelected(AnActionEvent anActionEvent, Class<T> tClass) {
        if (anActionEvent == null) {
            return Optional.empty();
        }

        Component component = anActionEvent.getData(PlatformDataKeys.CONTEXT_COMPONENT);
        /*
            Java 10 模式匹配（Pattern Matching）特性
            table 被声明为局部变量，并且只有当 component 实际上是 TableView<?> 的实例时，它才会被初始化
         */
        if (!(component instanceof TableView<?> table)) {
            return Optional.empty();
        }

        Object selectedObject = table.getSelectedObject();
        if (selectedObject == null) {
            return Optional.empty();
        }

        return Optional.of(tClass.cast(selectedObject));
    }
}

