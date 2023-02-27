package com.wanglj.lmm.admin.api.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 机构信息
 */
@Data
public class SysOrganization implements Serializable {

    /**
     * 主键id
     */
    @ExcelIgnore
    @TableId(value = "org_id", type = IdType.ASSIGN_ID)
    private Long orgId;

    /**
     * 机构名称
     */
    @ExcelProperty(index = 0,value = "机构名称")
    private String orgName;

    /**
     * 机构编码
     */
    @ExcelProperty(index = 1,value = "机构编码")
    private String orgCode;

    /**
     * 机构等级
     */
    @ExcelProperty(index = 10,value = "机构等级")
    private String orgLevel;

    /**
     * 机构类型
     */
    @ExcelProperty(value = "机构类型")
    private String orgType;

    /**
     * 上级机构
     */
    @ExcelProperty(value = "上级机构")
    private String parentOrgId;

    /**
     * 省份
     */
    @ExcelProperty(value = "省份")
    private String province;

    /**
     * 地市
     */
    @ExcelProperty(value = "地市")
    private String city;

    /**
     * 区（县）
     */
    @ExcelProperty(value = "区（县）")
    private String county;

    /**
     * 乡（镇、街道）
     */
    @ExcelProperty(value = "乡（镇、街道）")
    private String town;

    /**
     * 村（社区）
     */
    @ExcelProperty(value = "村（社区）")
    private String village;

    /**
     * 详细地址
     */
    @ExcelProperty(value = "详细地址")
    private String fullAddress;

    /**
     * 互认服务启用状态   0-不启用，1-启用
     */
    @ExcelProperty(value = "互认服务启用状态")
    private Integer MutualRecStatus;

    /**
     * 创建人
     */
    @ExcelIgnore
    private String  createBy;

    /**
     * 修改人
     */
    @ExcelProperty(value = "最后修改人")
    private String updateBy;

    /**
     * 创建时间
     */
    @ExcelIgnore
    private Date createTime;

    /**
     * 修改时间
     */
    @ColumnWidth(23)
    @ExcelProperty(value = "最后修改时间")
    private Date updateTime;

    /**
     * 0-正常，1-删除
     */
    @ExcelIgnore
    @TableLogic
    private Integer delFlag;

    /**
     * 是否存在子级  1-存在 0-否
     */
    @TableField(exist = false)
    private Integer hasChildren = 0;

}
