package com.wanglj.lmm.admin.api.req.taskmanagement;

import com.wanglj.lmm.admin.api.req.base.BasePageRequest;
import lombok.Data;

/**
 * 平台任务管理分页参数
 */
@Data
public class TaskManagementPageReq extends BasePageRequest {
    /**
     * 任务名称
     */
    private String tname;

    /**
     * 运行状态
     */
    private String tstatus;


    /**
     * 配置时间开始
     */
    private String loginBegin;

    /**
     * 配置时间结束
     */
    private String loginEnd;
}
