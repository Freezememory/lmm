package com.wanglj.lmm.admin.api.req.user;

import lombok.Data;

/**
 * 管理后台用户
 */
@Data
public class SysUserReq {

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机
     */
    private String phone;
}
