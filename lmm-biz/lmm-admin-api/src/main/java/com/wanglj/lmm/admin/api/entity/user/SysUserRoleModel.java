package com.wanglj.lmm.admin.api.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统用户角色中间表
 */
@Data
@TableName("sys_user_role")
public class SysUserRoleModel implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
