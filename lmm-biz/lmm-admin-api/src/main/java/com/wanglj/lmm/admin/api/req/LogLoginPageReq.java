package com.wanglj.lmm.admin.api.req;

import com.wanglj.lmm.admin.api.req.base.BasePageRequest;
import lombok.Data;

/**
 * 登录日志管理分页参数
 */
@Data
public class LogLoginPageReq extends BasePageRequest {
    /**
     * 登录端
     */
    private String loginSide;

    /**
     * 登录状态
     */
    private String status;


    /**
     * 登录时间
     */
    private String loginBegin;

    /**
     * 登时间
     */
    private String loginEnd;
}
