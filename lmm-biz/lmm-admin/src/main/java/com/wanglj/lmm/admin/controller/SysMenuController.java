package com.wanglj.lmm.admin.controller;

import com.wanglj.lmm.admin.service.SysMenuService;
import com.wanglj.lmm.common.base.constant.BfjConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统菜单管理
 */
@RestController
@RequestMapping(BfjConstants.ADMIN_URL_PREFIX + "/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;
}
