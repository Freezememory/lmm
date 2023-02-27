package com.wanglj.lmm.admin.api.req;

import com.wanglj.lmm.admin.api.req.base.BasePageRequest;
import lombok.Data;

/**
 * 系统角色分页参数
 */
@Data
public class SysRolePageReq extends BasePageRequest {
    /**
     * 手机
     */
    private String roleName;
}
