package com.wanglj.lmm.auth.api.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户登录对象
 */
@Data
public class LoginBody {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 20, message = "账户长度必须在2到20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9@.]+$", message = "账号只能英文或数字或邮箱")
    @Size()
    private String username;

    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空")
    @Length(min = 6, max = 20, message = "用户密码长度必须在6到20个字符之间")
    private String password;
}
