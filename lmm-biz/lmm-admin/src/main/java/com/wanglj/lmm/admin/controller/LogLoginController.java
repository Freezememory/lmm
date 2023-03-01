package com.wanglj.lmm.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglj.lmm.admin.service.LogLoginService;
import com.wanglj.lmm.common.base.constant.LmmConstants;
import com.wanglj.lmm.common.base.util.BeanUtilsExt;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.admin.api.entity.LogLoginModel;
import com.wanglj.lmm.admin.api.enums.AdminCode;
import com.wanglj.lmm.admin.api.req.LogLoginPageReq;
import com.wanglj.lmm.admin.api.vo.base.BasePageVO;
import com.wanglj.lmm.admin.api.vo.LogLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录日志管理
 */
@RestController
@RequestMapping(LmmConstants.ADMIN_URL_PREFIX + "/logLogin")
public class LogLoginController {

    @Autowired
    private LogLoginService logLoginService;

    /**
     * 登录日志管理分页接口
     *
     * @param req
     * @return {@link R<BasePageVO< LogLoginVO >>}
     */
    @PostMapping("/page")
    public R<BasePageVO<LogLoginVO>> pageLogLogin(@RequestBody LogLoginPageReq req) {
        Page<LogLoginModel> page = logLoginService.pageLogLogin(req);
        return R.ok(AdminCode.Success, BasePageVO.build(page, BeanUtilsExt.copyList(page.getRecords(), LogLoginVO.class).get()));
    }
}
