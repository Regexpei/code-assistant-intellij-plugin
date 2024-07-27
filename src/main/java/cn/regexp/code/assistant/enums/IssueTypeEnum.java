package cn.regexp.code.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Regexpei
 * @date 2024/7/7 19:14
 * @description 问题类型枚举
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum IssueTypeEnum implements BaseEnum {

    PERFORMANCE(1, "性能"),

    FUNCTION(2, "功能"),

    UI(3, "UI"),

    SECURITY(4, "安全"),

    OTHER(5, "其他");


    private final int code;

    private final String desc;


    private static final Map<Integer, String> CODE_TO_DESC_MAP = new HashMap<>();

    static {
        for (IssueTypeEnum value : IssueTypeEnum.values()) {
            CODE_TO_DESC_MAP.put(value.getCode(), value.getDesc());
        }
    }

    /**
     * 根据 code 获取描述
     *
     * @param code code
     * @return 描述
     */
    public static String getDesc(int code) {
        return CODE_TO_DESC_MAP.getOrDefault(code, "");
    }

}
