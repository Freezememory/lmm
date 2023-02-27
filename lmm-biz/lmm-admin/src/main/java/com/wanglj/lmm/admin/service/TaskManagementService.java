package com.wanglj.lmm.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.api.entity.TaskManagementModel;
import com.wanglj.lmm.admin.api.req.taskmanagement.TaskManagementPageReq;
import com.wanglj.lmm.admin.api.req.taskmanagement.TaskManagementReq;

/**
 * 平台任务管理
 */
public interface TaskManagementService {
    /**
     * 平台任务管理分页查询
     * @param req
     * @return
     */
    Page<TaskManagementModel> pageTaskManagement(TaskManagementPageReq req);

    /**
     * 新增任务管理
     * @param req
     * @return
     */
    Long saveTaskManagement(TaskManagementReq req);

    /**
     * 删除任务管理
     * @param req
     * @return
     */
    boolean deleteTaskManagement(TaskManagementReq req);

    /**
     * 编辑任务管理
     * @param req
     * @return
     */
    boolean updateTaskManagement(TaskManagementReq req);
}
