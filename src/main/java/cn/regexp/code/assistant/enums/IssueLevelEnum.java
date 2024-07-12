package cn.regexp.code.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Regexpei
 * @date 2024/7/7 19:20
 * @description 问题级别枚举
 * @since 1.0.0
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


    /**
     * 根据 code 获取描述
     *
     * @param code code
     * @return 描述
     */
    public static String getDesc(int code) {
        for (IssueLevelEnum value : IssueLevelEnum.values()) {
            if (value.getCode() == code) {
                return value.getDesc();
            }
        }
        return null;
    }
}
