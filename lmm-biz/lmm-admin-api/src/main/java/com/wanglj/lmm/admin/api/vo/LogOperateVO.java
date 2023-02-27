package com.wanglj.lmm.admin.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * 操作日志管理VO
 */
@Data
public class LogOperateVO {

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
     * 操作时间
     */
    private Date operateTime;

    /**
     * 用户ip(即登录ip)
     */
    private String loginIp;

    /**
     * 操作端（即登录端）
     */
    private String loginSide;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 操作详情
     */
    private String operateDetail;

    /**
     * 操作结果
     */
    private String operateResult;
    /**
     * 接口参数信息
     */
    private String interfaceParameter;

    /**
     * 系统返回信息
     */
    private String resultInfo;
}
