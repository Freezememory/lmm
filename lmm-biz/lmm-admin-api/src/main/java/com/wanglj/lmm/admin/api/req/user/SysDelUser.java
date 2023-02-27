package com.wanglj.lmm.admin.api.req.user;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 删除用户
 */
@Data
public class SysDelUser {
    /**
     * 删除用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    private Long userId;
}
