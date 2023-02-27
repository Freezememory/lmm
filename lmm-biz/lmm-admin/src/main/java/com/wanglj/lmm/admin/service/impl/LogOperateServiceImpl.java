package com.wanglj.lmm.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglj.lmm.admin.mapper.LogOperateMapper;
import com.wanglj.lmm.admin.api.entity.LogOperateModel;
import com.wanglj.lmm.admin.api.req.LogOperatePageReq;
import com.wanglj.lmm.admin.service.LogOperateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogOperateServiceImpl extends ServiceImpl<LogOperateMapper, LogOperateModel> implements LogOperateService {

    @Override
    public Page<LogOperateModel> pageLogOperate(LogOperatePageReq req) {
        Page<LogOperateModel> page = new Page<>(req.getPage(), req.getLimit());
        return baseMapper.selectPage(page, Wrappers.lambdaQuery(LogOperateModel.class)
                .eq(StrUtil.isNotEmpty(req.getLoginSide()), LogOperateModel::getLoginSide, req.getLoginSide())
                .eq(StrUtil.isNotEmpty(req.getOperateResult()), LogOperateModel::getOperateResult, req.getOperateResult())
                .apply(StrUtil.isNotEmpty(req.getLoginBegin()),"UNIX_TIMESTAMP(operate_time)>=UNIX_TIMESTAMP('" + req.getLoginBegin() + "')")
                .apply(StrUtil.isNotEmpty(req.getLoginEnd()),"UNIX_TIMESTAMP(operate_time)<=UNIX_TIMESTAMP('" + req.getLoginEnd() + "')")
                .orderByDesc(LogOperateModel::getOid));
    }
}
