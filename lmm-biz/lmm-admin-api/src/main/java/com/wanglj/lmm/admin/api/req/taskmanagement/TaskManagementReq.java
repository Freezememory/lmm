package com.wanglj.lmm.admin.api.req.taskmanagement;

import com.wanglj.lmm.admin.api.req.base.BasePageRequest;
import lombok.Data;

import java.util.Date;

/**
 * 平台任务管理功能参数，适用于增加、删除（只传tid）、修改（与增加类似但必传tid）三个功能
 */
@Data
public class TaskManagementReq extends BasePageRequest {

    /**
     * 平台任务管理主键id
     */
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
