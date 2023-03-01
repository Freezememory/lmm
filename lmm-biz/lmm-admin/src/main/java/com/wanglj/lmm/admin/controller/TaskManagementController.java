package com.wanglj.lmm.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.api.entity.TaskManagementModel;
import com.wanglj.lmm.admin.api.enums.AdminCode;
import com.wanglj.lmm.admin.api.req.taskmanagement.TaskManagementPageReq;
import com.wanglj.lmm.admin.api.req.taskmanagement.TaskManagementReq;
import com.wanglj.lmm.admin.api.vo.base.BasePageVO;
import com.wanglj.lmm.admin.api.vo.TaskManagementVO;
import com.wanglj.lmm.admin.service.TaskManagementService;
import com.wanglj.lmm.common.base.constant.LmmConstants;
import com.wanglj.lmm.common.base.util.BeanUtilsExt;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.common.base.util.SimpleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台任务管理
 */
@RestController
@RequestMapping(LmmConstants.ADMIN_URL_PREFIX + "/taskManagement")
public class TaskManagementController {

    @Autowired
    private TaskManagementService taskManagementService;

    /**
     * 平台任务管理分页接口
     *
     * @param req
     * @return {@link R<BasePageVO< TaskManagementVO >>}
     */
    @PostMapping("/page")
    public R<BasePageVO<TaskManagementVO>> pageTaskManagement(@RequestBody TaskManagementPageReq req) {
        Page<TaskManagementModel> page = taskManagementService.pageTaskManagement(req);
        return R.ok(AdminCode.Success, BasePageVO.build(page, BeanUtilsExt.copyList(page.getRecords(), TaskManagementVO.class).get()));
    }


    /**
     * 新增任务管理
     * @param req
     * @return
     */
    @PostMapping("/save")
    public R<SimpleObject> saveTaskManagement(@RequestBody TaskManagementReq req) {
        return R.ok(AdminCode.Success, SimpleObject.build(taskManagementService.saveTaskManagement(req)));
    }


    /**
     * 删除任务管理
     *
     * @param req
     * @return {@link R<SimpleObject>}
     */
    @PostMapping("/delete")
    public R<SimpleObject> deleteTaskManagement(@RequestBody TaskManagementReq req) {
        return R.ok(AdminCode.Success, SimpleObject.build(taskManagementService.deleteTaskManagement(req)));
    }

    /**
     * 编辑任务管理
     * @param req 更新机构信息
     * @return: Boolean
     */
    @PostMapping("/update")
    public R<SimpleObject> updateTaskManagement(@RequestBody TaskManagementReq req) {
        return R.ok(AdminCode.Success, SimpleObject.build(taskManagementService.updateTaskManagement(req)));
    }
}


