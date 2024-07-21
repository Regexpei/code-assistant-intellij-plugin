package cn.regexp.code.assistant.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Regexpei
 * @date 2024/7/20 22:18
 * @description 统一响应结果类
 * @since 1.0.0
 */
@Setter
@Getter
public class Result<T> {
    /**
     * 状态码
     */
    private int code;
    /**
     * 信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    public boolean isSuccess() {
        return code == 200;
    }
}
