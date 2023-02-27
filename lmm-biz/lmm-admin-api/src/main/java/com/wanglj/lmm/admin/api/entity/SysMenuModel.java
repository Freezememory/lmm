package com.wanglj.lmm.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanglj.lmm.admin.api.entity.base.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统菜单表
 */
@Data
@TableName("sys_menu")
public class SysMenuModel extends BaseModel implements Serializable {

    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单权限标识
     */
    private String permission;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 前端URL
     */
    private String path;

    /**
     * 排序值
     */
    private Integer sortOrder;

    /**
     * 菜单类型 （0菜单 1按钮）
     */
    private Boolean type;

    /**
     * 0--正常 1--删除
     */
    private String delFlag;
}
