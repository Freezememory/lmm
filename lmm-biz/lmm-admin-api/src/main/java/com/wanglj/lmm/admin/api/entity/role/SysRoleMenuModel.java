package com.wanglj.lmm.admin.api.entity.role;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统菜单角色中间表
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenuModel implements Serializable {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
