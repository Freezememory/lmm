package com.wanglj.lmm.admin.api.entity.role;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanglj.lmm.admin.api.entity.base.BaseModel;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_role")
public class SysRoleModel extends BaseModel implements Serializable {

    @TableId(value = "role_id", type = IdType.AUTO)
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

    /**
     * 删除标识（0-正常,1-删除）
     */
    private Boolean delFlag;
}
