package com.wanglj.lmm.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglj.lmm.admin.api.req.user.SysUserReq;
import com.wanglj.lmm.admin.mapper.SysUserMapper;
import com.wanglj.lmm.admin.api.entity.user.SysUserModel;
import com.wanglj.lmm.admin.api.req.user.SysUserPageReq;
import com.wanglj.lmm.admin.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserModel> implements SysUserService {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Override
    public Page<SysUserModel> pageUser(SysUserPageReq req) {
        Page<SysUserModel> page = new Page<>(req.getPage(), req.getLimit());
        return baseMapper.selectPage(page, Wrappers.lambdaQuery(SysUserModel.class)
                .eq(StrUtil.isNotEmpty(req.getPhone()), SysUserModel::getPhone, req.getPhone())
                .orderByDesc(SysUserModel::getUserId));
    }

    @Override
    public SysUserModel selectUserByUsername(String username) {
        return baseMapper.selectOne(Wrappers.<SysUserModel>query().lambda().eq(SysUserModel::getUsername, username));
    }

    @Override
    public Long saveUser(SysUserReq req) {
        SysUserModel user = new SysUserModel();
        user.setPassword(ENCODER.encode(req.getPassword()));
        user.setUsername(req.getUsername());
        user.setPhone(req.getPhone());
        user.setCreateTime(new Date());
        user.setDelFlag(0);
        user.setLockFlag(0);
        baseMapper.insert(user);
        return user.getUserId();
    }

    @Override
    public SysUserModel selectUserById(Long userId) {
        return baseMapper.selectOne(Wrappers.<SysUserModel>query().lambda().eq(SysUserModel::getUserId, userId));
    }

    @Override
    public Integer delUserById(Long userId) {
        return baseMapper.deleteById(userId);
    }

    @Override
    public Integer updateUser(SysUserModel user, Boolean needUpdatePassword) {
        if (needUpdatePassword) {
            user.setPassword(ENCODER.encode(user.getPassword()));
        }
        return baseMapper.updateById(user);
    }
}
