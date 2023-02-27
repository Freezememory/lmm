package com.wanglj.lmm.admin.api.req;

import com.wanglj.lmm.admin.api.req.base.BasePageRequest;
import lombok.Data;

/**
 * 机构信息
 */
@Data
public class SysOrgReq  extends BasePageRequest {


    /**
     * 机构id
     */
    private String orgId;


    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 机构等级
     */
    private String orgLevel;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 上级机构
     */
    private String parentOrgId;

    /**
     * 省份
     */
    private String province;

    /**
     * 地市
     */
    private String city;

    /**
     * 区（县）
     */
    private String county;

    /**
     * 互认服务启用状态   0-不启用，1-启用
     */
    private Integer MutualRecStatus;


}
