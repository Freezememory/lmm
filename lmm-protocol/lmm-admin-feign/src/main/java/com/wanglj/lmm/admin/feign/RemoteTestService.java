package com.wanglj.lmm.admin.feign;


import com.wanglj.lmm.common.base.constant.LmmConstants;
import com.wanglj.lmm.common.base.enums.SecurityConstants;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.common.base.util.SimpleObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = "remoteTestService", value = LmmConstants.PORTAL_MODULE)
public interface RemoteTestService {

    /**
     * 通过用户名查询用户、角色信息
     * @return R
     */
    @PostMapping(LmmConstants.PORTAL_URL_PREFIX + "/test/info1")
    R<SimpleObject> info1(@RequestBody SimpleObject info, @RequestHeader(SecurityConstants.FROM) String from);



    @PostMapping(LmmConstants.PORTAL_URL_PREFIX + "/test/info2")
    R<SimpleObject> info2(@RequestBody SimpleObject info, @RequestHeader(SecurityConstants.FROM) String from);

}
