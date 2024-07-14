package cn.regexp.code.assistant.listener;

import com.intellij.util.messages.Topic;

/**
 * @author Regexpei
 * @date 2024/7/13 12:05
 * @description 刷新监听器
 * @since 1.0.0
 */
@FunctionalInterface
public interface RefreshListener {
    /**
     * 添加问题
     */
    Topic<RefreshListener> ADD_ISSUE_TOPIC = Topic.create("Add Issue", RefreshListener.class);

    /**
     * 刷新列表
     */
    void refresh();

}
