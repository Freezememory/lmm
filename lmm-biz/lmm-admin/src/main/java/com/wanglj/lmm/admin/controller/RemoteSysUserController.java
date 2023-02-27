package com.wanglj.lmm.admin.controller;

import com.wanglj.lmm.admin.protocol.sysuser.SysUserRemote;
import com.wanglj.lmm.common.base.util.BeanUtilsExt;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.admin.service.SysUserService;
import com.wanglj.lmm.common.base.constant.BfjConstants;
import com.wanglj.lmm.admin.api.entity.user.SysUserModel;
import com.wanglj.lmm.admin.api.enums.AdminCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BfjConstants.ADMIN_URL_PREFIX + "/remote/sysUser")
public class RemoteSysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取指定用户全部信息
     */
    @GetMapping("/info/{username}")
    public R<SysUserRemote> info(@PathVariable String username) {
        SysUserModel user = sysUserService.selectUserByUsername(username);
        if (user == null) {
            return R.fail(AdminCode.SysUserNotExist);
        }
        return R.ok(AdminCode.Success, BeanUtilsExt.copyBean(user, SysUserRemote.class).get());
    }
}
