package com.wanglj.lmm.admin.api.vo;

import lombok.Data;

/**
 * 角色VO
 */
@Data
public class SysRoleVO {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String roleDesc;
}
