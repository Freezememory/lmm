package com.wanglj.lmm.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.api.req.user.SysDelUser;
import com.wanglj.lmm.admin.api.req.user.SysUserReq;
import com.wanglj.lmm.admin.api.req.user.SysUserUptReq;
import com.wanglj.lmm.admin.service.SysUserService;
import com.wanglj.lmm.common.base.constant.BfjConstants;
import com.wanglj.lmm.common.base.util.BeanUtilsExt;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.admin.api.entity.user.SysUserModel;
import com.wanglj.lmm.admin.api.enums.AdminCode;
import com.wanglj.lmm.admin.api.req.user.SysUserPageReq;
import com.wanglj.lmm.admin.api.vo.base.BasePageVO;
import com.wanglj.lmm.admin.api.vo.SysUserVO;
import com.wanglj.lmm.common.base.util.SimpleObject;
import com.wanglj.lmm.common.satoken.utils.LoginHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统用户管理
 */
@RestController
@RequestMapping(BfjConstants.ADMIN_URL_PREFIX + "/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 系统用户分页接口
     *
     * @param req
     * @return {@link R<BasePageVO<SysUserVO>>}
     */
    @PostMapping("/page")
    public R<BasePageVO<SysUserVO>> pageUser(@RequestBody SysUserPageReq req) {
        Page<SysUserModel> page = sysUserService.pageUser(req);
        return R.ok(AdminCode.Success, BasePageVO.build(page, BeanUtilsExt.copyList(page.getRecords(), SysUserVO.class).get()));
    }

    /**
     * 创建用户
     *
     * @param req
     * @return {@link R<SimpleObject>}
     */
    @PostMapping("/save")
    public R<SimpleObject> saveUser(@RequestBody SysUserReq req) {
        SysUserModel oldUser = sysUserService.selectUserByUsername(req.getUsername());
        if (oldUser != null) {
            return R.fail(AdminCode.SysUserNameExist);
        }
        return R.ok(AdminCode.Success, SimpleObject.build(sysUserService.saveUser(req)));
    }

    /**
     * 更新用户
     *
     * @param req
     * @return {@link R<SimpleObject>}
     */
    @PostMapping("/update")
    public R<SimpleObject> updateUser(@RequestBody SysUserUptReq req) {
        SysUserModel user = sysUserService.selectUserById(req.getUserId());
        if (user != null) {
            return R.fail(AdminCode.SysUserNameExist);
        }
        if (StrUtil.isNotEmpty(req.getPassword())) {
            user.setPassword(req.getPassword());
        }
        user.setPhone(req.getPhone());
        return R.ok(AdminCode.Success, SimpleObject.build(sysUserService.updateUser(user, StrUtil.isNotEmpty(req.getPassword()))));
    }

    /**
     * 用户信息
     *
     * @return {@link R<SysUserVO>}
     */
    @GetMapping("/info")
    public R<SysUserVO> info() {
        SysUserModel user = sysUserService.selectUserByUsername(LoginHelper.getUsername());
        if (user != null) {
            return R.fail(AdminCode.SysUserNameExist);
        }
        return R.ok(AdminCode.Success, BeanUtilsExt.copyBean(user, SysUserVO.class).get());
    }

    /**
     * 根据用户ID查询用户信息
     *
     * @return {@link R<SysUserVO>}
     */
    @GetMapping("/detail/{userId}")
    public R<SysUserVO> detail(@PathVariable Long userId) {
        SysUserModel user = sysUserService.selectUserById(userId);
        if (user != null) {
            return R.fail(AdminCode.SysUserNameExist);
        }
        return R.ok(AdminCode.Success, BeanUtilsExt.copyBean(user, SysUserVO.class).get());
    }

    /**
     * 删除用户
     *
     * @param req
     * @return {@link R<SimpleObject>}
     */
    @PostMapping("/delUser")
    public R<SimpleObject> delUser(@RequestBody SysDelUser req) {
        SysUserModel user = sysUserService.selectUserById(req.getUserId());
        if (user != null) {
            return R.fail(AdminCode.SysUserNameExist);
        }
        return R.ok(AdminCode.Success, SimpleObject.build(sysUserService.delUserById(req.getUserId())));
    }
}
