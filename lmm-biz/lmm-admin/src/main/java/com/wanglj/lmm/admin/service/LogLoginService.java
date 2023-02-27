package com.wanglj.lmm.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.api.entity.LogLoginModel;
import com.wanglj.lmm.admin.api.req.LogLoginPageReq;

/**
 * 登录日志管理
 */
public interface LogLoginService {
    /**
     * 登录日志管理分页查询
     * @param req
     * @return
     */
    Page<LogLoginModel> pageLogLogin(LogLoginPageReq req);
}
