package com.wanglj.lmm.admin.api.req.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新用户
 */
@Data
public class SysUserUptReq {

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    private Long userId;

    /**
     * 密码(前台用户没有修改,则不用传参)
     */
    private String password;

    /**
     * 手机
     */
    private String phone;
}
