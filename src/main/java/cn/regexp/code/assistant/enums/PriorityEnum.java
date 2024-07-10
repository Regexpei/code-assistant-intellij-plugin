package cn.regexp.code.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Regexpei
 * @date 2024/7/7 19:25
 * @description 优先级枚举
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum PriorityEnum {
    HIGH(1, "高"),
    MEDIUM(2, "中"),
    LOW(3, "低");

    private final int code;
    private final String desc;

}
