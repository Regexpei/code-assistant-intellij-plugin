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

    /**
     * 根据 code 获取描述
     *
     * @param code code
     * @return 描述
     */
    public static String getDesc(int code) {
        for (PriorityEnum value : PriorityEnum.values()) {
            if (value.getCode() == code) {
                return value.getDesc();
            }
        }
        return null;
    }
}
