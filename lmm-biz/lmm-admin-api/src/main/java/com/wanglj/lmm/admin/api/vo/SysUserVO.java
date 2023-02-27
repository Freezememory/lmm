package com.wanglj.lmm.admin.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * 系统用户VO
 */
@Data
public class SysUserVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 手机
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 0-正常，9-锁定
     */
    private Integer lockFlag;

    /**
     * 0-正常，1-删除
     */
    private Integer delFlag;
}
