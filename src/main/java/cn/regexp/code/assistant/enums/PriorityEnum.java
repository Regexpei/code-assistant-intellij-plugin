package cn.regexp.code.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Regexpei
 * @date 2024/7/7 19:25
 * @description 优先级枚举
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum PriorityEnum implements BaseEnum {
    HIGH(1, "高"),
    MEDIUM(2, "中"),
    LOW(3, "低");

    private final int code;
    private final String desc;

    private static final Map<Integer, String> CODE_TO_DESC_MAP = new HashMap<>();

    static {
        for (PriorityEnum value : PriorityEnum.values()) {
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
