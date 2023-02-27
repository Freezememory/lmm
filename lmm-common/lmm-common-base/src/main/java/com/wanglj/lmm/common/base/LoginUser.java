package com.wanglj.lmm.common.base;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 权限标识集合
     */
    private List<String> menuPermissions;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色集合 roleCode
     */
    private List<String> roles;

    /**
     * 角色集合 roleCode
     */
    private List<Long> roleIds;

    /**
     * 角色集合 sysRole对象
     */
    private List roleList;

    /**
     * 岗位集合 对象
     */
    private List postList;

    /**
     * 获取登录id
     */
    public String getLoginId() {
        return userType + ":" + userId;
    }

}

