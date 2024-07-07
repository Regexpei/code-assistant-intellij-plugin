package cn.regexp.code.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Regexpei
 * @date 2024/7/7 19:21
 * @description 问题状态枚举
 */
@Getter
@AllArgsConstructor
public enum IssueStatusEnum {
    NEW(1, "新建"),
    ASSIGNED(2, "已分配"),
    PROCESSING(3, "处理中"),
    SOLVED(4, "已解决"),
    CLOSED(5, "关闭");

    private final int code;

    private final String desc;
}
