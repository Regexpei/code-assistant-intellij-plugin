package cn.regexp.code.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Regexpei
 * @date 2024/7/7 19:14
 * @description 问题类型枚举
 * @since 1.0.0
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

    /**
     * 根据 code 获取描述
     *
     * @param code code
     * @return 描述
     */
    public static String getDesc(int code) {
        for (IssueTypeEnum value : IssueTypeEnum.values()) {
            if (value.getCode() == code) {
                return value.getDesc();
            }
        }
        return null;
    }

}
