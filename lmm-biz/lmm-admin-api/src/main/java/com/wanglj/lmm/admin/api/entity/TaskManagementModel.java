package com.wanglj.lmm.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 平台任务管理
 */
@Data
@TableName("task_management")
public class TaskManagementModel implements Serializable {

    @TableId(value = "tid", type = IdType.AUTO)
    private Long tid;

    /**
     * 任务名称
     */
    private String tname;

    /**
     * 任务描述
     */
    private String tdesc;

    /**
     * 执行代码模块
     */
    private String tCodeModule;

    /**
     * 运行状态
     */
    private String tstatus;

    /**
     * 创建时间
     */
    private Date ttime;

    /**
     * 有效期开始时间
     */
    private Date expirationTimeBegin;

    /**
     * 有效期结束时间
     */
    private Date expirationTimeEnd;

    /**
     * 执行时段开始时间
     */
    private Date tOperateTimeBegin;

    /**
     * 执行时段结束时间
     */
    private Date tOperateTimeEnd;

}
