package com.wanglj.lmm.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglj.lmm.admin.api.req.taskmanagement.TaskManagementReq;
import com.wanglj.lmm.admin.mapper.TaskManagementMapper;
import com.wanglj.lmm.admin.api.entity.TaskManagementModel;
import com.wanglj.lmm.admin.api.req.taskmanagement.TaskManagementPageReq;
import com.wanglj.lmm.admin.service.TaskManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class TaskManagementServiceImpl extends ServiceImpl<TaskManagementMapper, TaskManagementModel> implements TaskManagementService {

    @Override
    public  Page<TaskManagementModel> pageTaskManagement(TaskManagementPageReq req) {
        Page<TaskManagementModel> page = new Page<>(req.getPage(), req.getLimit());
        return baseMapper.selectPage(page, Wrappers.lambdaQuery(TaskManagementModel.class)
                .eq(StrUtil.isNotEmpty(req.getTname()), TaskManagementModel::getTname, req.getTname())
                .eq(StrUtil.isNotEmpty(req.getTstatus()), TaskManagementModel::getTstatus, req.getTstatus())
                .apply(StrUtil.isNotEmpty(req.getLoginBegin()),"UNIX_TIMESTAMP(ttime)>=UNIX_TIMESTAMP('" + req.getLoginBegin() + "')")
                .apply(StrUtil.isNotEmpty(req.getLoginEnd()),"UNIX_TIMESTAMP(ttime)<=UNIX_TIMESTAMP('" + req.getLoginEnd() + "')")
                .orderByDesc(TaskManagementModel::getTid));
    }

    @Override
    public Long saveTaskManagement(TaskManagementReq req) {
        TaskManagementModel taskManagementModel = new TaskManagementModel();
        BeanUtils.copyProperties(req, taskManagementModel);
        taskManagementModel.setTtime(new Date());
        baseMapper.insert(taskManagementModel);
        return taskManagementModel.getTid();
    }

    @Override
    public boolean deleteTaskManagement(TaskManagementReq req) {
        return  super.removeById(req.getTid());
    }

    @Override
    public boolean updateTaskManagement(TaskManagementReq req) {
        TaskManagementModel taskManagementModel = new TaskManagementModel();
        BeanUtils.copyProperties(req, taskManagementModel);
        return super.updateById(taskManagementModel);
    }
}
