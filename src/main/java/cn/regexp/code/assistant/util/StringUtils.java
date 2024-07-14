package cn.regexp.code.assistant.util;

/**
 * @author Regexpei
 * @date 2024/7/14 16:02
 * @description 字符串处理工具类
 * @since 1.0.0
 */
public class StringUtils {

    private StringUtils() {}


    /**
     * 去掉多行字符串开头的连续 0 到 4 个空格
     *
     * @param str 待处理字符串
     * @return 已处理字符串
     */
    public static String trimMultiLineStartBlank(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        if (!str.contains("\n")) {
            return trimSingleLineStartBlank(str);
        }

        StringBuilder sb = new StringBuilder();
        for (String line : str.split("\n")) {
            sb.append(trimSingleLineStartBlank(line)).append("\n");
        }

        return sb.toString();
    }


    /**
     * 去掉单行字符串开头的连续 0 到 4 个空格
     *
     * @param str 待处理字符串
     * @return 已处理字符串
     */
    public static String trimSingleLineStartBlank(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.replaceFirst(" {0,4}", "");
    }
}
