package cn.regexp.code.assistant.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Regexpei
 * @date 2024/7/25 21:03
 * @description 用户
 * @since 1.0.0
 */
@Getter
@Setter
public class User {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户邮件
     */
    private String email;

}
