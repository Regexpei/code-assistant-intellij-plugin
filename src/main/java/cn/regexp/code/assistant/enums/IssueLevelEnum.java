package cn.regexp.code.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Regexpei
 * @date 2024/7/7 19:20
 * @description 问题级别枚举
 */
@Getter
@AllArgsConstructor
public enum IssueLevelEnum {
    MINOR(1, "轻微"),
    MAJOR(2, "重要"),
    CRITICAL(3, "严重"),
    FATAL(4, "致命"),
    ;

    private final int code;
    private final String desc;

}
