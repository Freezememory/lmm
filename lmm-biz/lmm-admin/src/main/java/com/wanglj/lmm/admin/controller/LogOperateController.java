package com.wanglj.lmm.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.service.LogOperateService;
import com.wanglj.lmm.common.base.constant.BfjConstants;
import com.wanglj.lmm.common.base.util.BeanUtilsExt;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.admin.api.entity.LogOperateModel;
import com.wanglj.lmm.admin.api.enums.AdminCode;
import com.wanglj.lmm.admin.api.req.LogOperatePageReq;
import com.wanglj.lmm.admin.api.vo.base.BasePageVO;
import com.wanglj.lmm.admin.api.vo.LogOperateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志管理
 */
@RestController
@RequestMapping(BfjConstants.ADMIN_URL_PREFIX + "/logOperate")
public class LogOperateController {

    @Autowired
    private LogOperateService logOperateService;

    /**
     * 操作日志管理分页接口
     *
     * @param req
     * @return {@link R<BasePageVO< LogOperateVO >>}
     */
    @PostMapping("/page")
    public R<BasePageVO<LogOperateVO>> pageLogLogin(@RequestBody LogOperatePageReq req) {
        Page<LogOperateModel> page = logOperateService.pageLogOperate(req);
        return R.ok(AdminCode.Success, BasePageVO.build(page, BeanUtilsExt.copyList(page.getRecords(), LogOperateVO.class).get()));
    }
}
