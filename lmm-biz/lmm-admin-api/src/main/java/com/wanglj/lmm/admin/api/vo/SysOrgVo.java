package com.wanglj.lmm.admin.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * 机构信息
 */
@Data
public class SysOrgVo {

    /**
     * 主键id
     */
    private Long orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 机构等级
     */
    private String orgLevel;

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
     * 乡（镇、街道）
     */
    private String town;

    /**
     * 村（社区）
     */
    private String village;

    /**
     * 详细地址
     */
    private String fullAddress;

    /**
     * 互认服务启用状态   0-不启用，1-启用
     */
    private Integer MutualRecStatus;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 0-正常，1-删除
     */
    private Integer delFlag;
}
