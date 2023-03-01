package com.wanglj.lmm.auth.controller;

import com.wanglj.lmm.auth.api.req.LoginBody;
import com.wanglj.lmm.auth.service.LoginService;
import com.wanglj.lmm.common.base.constant.LmmConstants;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.common.base.util.SimpleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录认证
 *
 */
@RestController
@RequestMapping(LmmConstants.AUTH_URL_PREFIX )
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 后台登陆
     */
    @PostMapping("/login")
    public R<SimpleObject> login(@Validated @RequestBody LoginBody loginBody) {
        return loginService.loginByAccount(loginBody);
    }
}
