package com.shanzhu.em.entity.form;

import lombok.Data;

/**
 * 用户登录表单
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Data
public class LoginForm {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
