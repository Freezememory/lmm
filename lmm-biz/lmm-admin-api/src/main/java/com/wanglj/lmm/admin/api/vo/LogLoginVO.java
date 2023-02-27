package com.wanglj.lmm.admin.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * 登录日志管理VO
 */
@Data
public class LogLoginVO {

    /**
     * 用户id
     */
    private String sid;

    /**
     * 用户名
     */
    private String sname;

    /**
     * 登录账号
     */
    private String saccount;

    /**
     * 角色
     */
    private String roleType;

    /**
     * 所属机构
     */
    private String organ;

    /**
     * 登录时间
     */
    private Date loginInTime;

    /**
     * 登出时间
     */
    private Date loginOutTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录端
     */
    private String loginSide;

    /**
     * 登录失败原因描述
     */
    private String loginErrorDes;
}
