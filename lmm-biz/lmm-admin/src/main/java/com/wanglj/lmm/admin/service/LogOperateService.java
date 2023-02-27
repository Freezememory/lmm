package com.wanglj.lmm.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.api.entity.LogOperateModel;
import com.wanglj.lmm.admin.api.req.LogOperatePageReq;

/**
 * 操作日志管理
 */
public interface LogOperateService {
    /**
     * 登录日志管理分页查询
     * @param req
     * @return
     */
    Page<LogOperateModel> pageLogOperate(LogOperatePageReq req);
}
