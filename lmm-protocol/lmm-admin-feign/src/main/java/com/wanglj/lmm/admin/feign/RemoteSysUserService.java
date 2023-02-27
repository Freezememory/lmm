package com.wanglj.lmm.admin.feign;

import com.wanglj.lmm.common.base.constant.BfjConstants;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.admin.protocol.sysuser.SysUserRemote;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "remoteUserService", value = BfjConstants.ADMIN_MODULE)
public interface RemoteSysUserService {

    /**
     * 通过用户名查询用户、角色信息
     * @param username 用户名
     * @return R
     */
    @GetMapping(BfjConstants.ADMIN_URL_PREFIX + "/remote/sysUser/info/{username}")
    R<SysUserRemote> info(@PathVariable("username") String username);
}
