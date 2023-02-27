package com.wanglj.lmm.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import com.wanglj.lmm.auth.api.req.LoginBody;
import com.wanglj.lmm.common.base.LoginUser;
import com.wanglj.lmm.common.base.enums.DeviceType;
import com.wanglj.lmm.common.base.enums.FrameCode;
import com.wanglj.lmm.common.base.util.BeanUtilsExt;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.common.base.util.SimpleObject;
import com.wanglj.lmm.common.satoken.utils.LoginHelper;
import com.wanglj.lmm.admin.feign.RemoteSysUserService;
import com.wanglj.lmm.admin.protocol.sysuser.SysUserRemote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private RemoteSysUserService remoteSysUserService;

    /**
     * 管理后台登录
     *
     * @param loginBody
     * @return
     */
    public R<SimpleObject> loginByAccount(LoginBody loginBody) {
        R<SysUserRemote> result = remoteSysUserService.info(loginBody.getUsername());
        if (result.getData() == null) {
            log.error("==SysUserRemote is null =={}", loginBody.getUsername());
            return R.fail(FrameCode.LoginFailed);
        }
        if (!ENCODER.matches(loginBody.getPassword(), result.getData().getPassword())) {
            return R.fail(FrameCode.AccountOrPwdFailed);
        }
        LoginHelper.loginByDevice(BeanUtilsExt.copyBean(result.getData(), LoginUser.class).get(), DeviceType.PC);
        // 获取登录token
        return R.ok(FrameCode.Success, SimpleObject.build(StpUtil.getTokenValue()));
    }
}
