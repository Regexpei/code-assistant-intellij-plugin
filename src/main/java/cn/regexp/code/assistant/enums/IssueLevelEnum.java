package cn.regexp.code.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Regexpei
 * @date 2024/7/7 19:20
 * @description 问题级别枚举
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum IssueLevelEnum implements BaseEnum {
    MINOR(1, "轻微"),
    MAJOR(2, "重要"),
    CRITICAL(3, "严重"),
    FATAL(4, "致命"),
    ;

    private final int code;
    private final String desc;


    private static final Map<Integer, String> CODE_TO_DESC_MAP = new HashMap<>();

    static {
        for (IssueLevelEnum value : IssueLevelEnum.values()) {
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
