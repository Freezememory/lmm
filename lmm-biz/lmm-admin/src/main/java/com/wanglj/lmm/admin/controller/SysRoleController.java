package com.wanglj.lmm.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.api.entity.role.SysRoleModel;
import com.wanglj.lmm.admin.api.enums.AdminCode;
import com.wanglj.lmm.admin.api.req.SysRolePageReq;
import com.wanglj.lmm.admin.api.vo.base.BasePageVO;
import com.wanglj.lmm.admin.api.vo.SysRoleVO;
import com.wanglj.lmm.admin.service.SysRoleService;
import com.wanglj.lmm.common.base.constant.LmmConstants;
import com.wanglj.lmm.common.base.util.BeanUtilsExt;
import com.wanglj.lmm.common.base.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统角色管理
 */
@RestController
@RequestMapping(LmmConstants.ADMIN_URL_PREFIX + "/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 角色用户分页接口
     *
     * @param req
     * @return {@link R < BasePageVO < SysRoleVO >>}
     */
    @PostMapping("/page")
    public R<BasePageVO<SysRoleVO>> pageUser(@RequestBody SysRolePageReq req) {
        Page<SysRoleModel> page = sysRoleService.pageRole(req);
        return R.ok(AdminCode.Success, BasePageVO.build(page, BeanUtilsExt.copyList(page.getRecords(), SysRoleVO.class).get()));
    }


}
