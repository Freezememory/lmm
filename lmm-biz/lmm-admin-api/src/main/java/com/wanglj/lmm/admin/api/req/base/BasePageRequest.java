package com.wanglj.lmm.admin.api.req.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class BasePageRequest {
    /**
     * 当页面
     */
    private Integer page = 1;

    /**
     * 分页大小
     */
    private Integer limit = 10;



    public <T> Page<T> page() {
        return new Page<>(page, limit);
    }
}
