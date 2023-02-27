package com.wanglj.lmm.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.api.entity.user.SysUserModel;
import com.wanglj.lmm.admin.api.req.user.SysUserPageReq;
import com.wanglj.lmm.admin.api.req.user.SysUserReq;

public interface SysUserService {
    /**
     * 系统用户分页查询
     * @param req
     * @return
     */
    Page<SysUserModel> pageUser(SysUserPageReq req);

    /**
     * 根据用户名查询
     */
    SysUserModel selectUserByUsername(String username);

    /**
     * 保存用户
     * @param req
     * @return
     */
    Long saveUser(SysUserReq req);

    /**
     * 根据用户ID查询用户信息
     * @param userId
     * @return
     */
    SysUserModel selectUserById(Long userId);

    /**
     * 根据用户ID删除用户
     * @param userId
     * @return
     */
    Integer delUserById(Long userId);

    /**
     * 更新用户
     * @param user
     * @param needUpdatePassword
     * @return
     */
    Integer updateUser(SysUserModel user, Boolean needUpdatePassword);
}
