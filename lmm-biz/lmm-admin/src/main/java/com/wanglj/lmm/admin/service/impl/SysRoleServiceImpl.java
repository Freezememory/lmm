package com.wanglj.lmm.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglj.lmm.admin.api.entity.role.SysRoleModel;
import com.wanglj.lmm.admin.api.req.SysRolePageReq;
import com.wanglj.lmm.admin.mapper.SysRoleMapper;
import com.wanglj.lmm.admin.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleModel> implements SysRoleService {

    @Override
    public Page<SysRoleModel> pageRole(SysRolePageReq req) {
        Page<SysRoleModel> page = new Page<>(req.getPage(), req.getLimit());
        return baseMapper.selectPage(page, Wrappers.lambdaQuery(SysRoleModel.class)
                .eq(StrUtil.isNotEmpty(req.getRoleName()), SysRoleModel::getRoleName, req.getRoleName())
                .orderByDesc(SysRoleModel::getRoleId));
    }
}
