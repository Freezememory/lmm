package com.wanglj.lmm.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.api.entity.role.SysRoleModel;
import com.wanglj.lmm.admin.api.req.SysRolePageReq;

public interface SysRoleService {
    /**
     *
     * @param req
     * @return
     */
    Page<SysRoleModel> pageRole(SysRolePageReq req);
}
