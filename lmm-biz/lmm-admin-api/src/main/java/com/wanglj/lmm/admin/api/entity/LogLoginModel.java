package com.wanglj.lmm.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志管理
 */
@Data
@TableName("log_login")
public class LogLoginModel implements Serializable {

    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

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
