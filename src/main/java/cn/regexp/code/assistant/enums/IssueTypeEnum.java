package cn.regexp.code.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Regexpei
 * @date 2024/7/7 19:14
 * @description 问题类型枚举
 */
@Getter
@AllArgsConstructor
public enum IssueTypeEnum {

    PERFORMANCE(1, "性能"),

    FUNCTION(2, "功能"),

    UI(3, "UI"),

    SECURITY(4, "安全"),

    OTHER(5, "其他");


    private final int code;

    private final String desc;



}
