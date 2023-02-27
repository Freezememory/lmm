package com.wanglj.lmm.admin.api.vo.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * Page<T>的封装
 */
@Data
public class BasePageVO<T> {
    /**
     * 列表
     */
    private List<T> records;

    /**
     * 总数
     */
    private Long total;

    /**
     * 分页大小
     */
    private Long size;

    /**
     * 当前页
     */
    private Long current;

    public static <T> BasePageVO<T> build(Page page, List<T> records) {
        BasePageVO vo = new BasePageVO();
        vo.setRecords(records);
        vo.setCurrent(page.getCurrent());
        vo.setSize(page.getSize());
        vo.setTotal(page.getTotal());
        return vo;
    }
}
