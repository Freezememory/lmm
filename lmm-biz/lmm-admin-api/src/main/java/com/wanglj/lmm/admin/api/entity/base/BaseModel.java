package com.wanglj.lmm.admin.api.entity.base;

import lombok.Data;

import java.util.Date;

@Data
public class BaseModel {

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private String updateTime;
}
