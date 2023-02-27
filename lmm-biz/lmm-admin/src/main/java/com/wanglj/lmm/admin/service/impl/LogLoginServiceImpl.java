package com.wanglj.lmm.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglj.lmm.admin.mapper.LogLoginMapper;
import com.wanglj.lmm.admin.api.entity.LogLoginModel;
import com.wanglj.lmm.admin.api.req.LogLoginPageReq;
import com.wanglj.lmm.admin.service.LogLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogLoginServiceImpl extends ServiceImpl<LogLoginMapper, LogLoginModel> implements LogLoginService {

    @Override
    public Page<LogLoginModel> pageLogLogin(LogLoginPageReq req) {
        Page<LogLoginModel> page = new Page<>(req.getPage(), req.getLimit());
        return baseMapper.selectPage(page, Wrappers.lambdaQuery(LogLoginModel.class)
                .eq(StrUtil.isNotEmpty(req.getLoginSide()), LogLoginModel::getLoginSide, req.getLoginSide())
                .eq(StrUtil.isNotEmpty(req.getStatus()), LogLoginModel::getStatus, req.getStatus())
                .apply(StrUtil.isNotEmpty(req.getLoginBegin()),"UNIX_TIMESTAMP(login_in_time)>=UNIX_TIMESTAMP('" + req.getLoginBegin() + "')")
                .apply(StrUtil.isNotEmpty(req.getLoginEnd()),"UNIX_TIMESTAMP(login_in_time)<=UNIX_TIMESTAMP('" + req.getLoginEnd() + "')")
                .orderByDesc(LogLoginModel::getLogId));
    }
}
