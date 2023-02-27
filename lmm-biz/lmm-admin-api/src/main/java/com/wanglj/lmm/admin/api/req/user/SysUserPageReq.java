package com.wanglj.lmm.admin.api.req.user;

import com.wanglj.lmm.admin.api.req.base.BasePageRequest;
import lombok.Data;

/**
 * 系统用户分页参数
 */
@Data
public class SysUserPageReq extends BasePageRequest {
    /**
     * 手机
     */
    private String phone;
}
