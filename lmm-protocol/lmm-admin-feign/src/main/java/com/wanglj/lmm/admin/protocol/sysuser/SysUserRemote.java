package com.wanglj.lmm.admin.protocol.sysuser;

import lombok.Data;

@Data
public class SysUserRemote {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 0-正常，9-锁定
     */
    private Integer lockFlag;

    /**
     * 0-正常，1-删除
     */
    private Integer delFlag;
}
