package com.wanglj.lmm.common.base.util;

import lombok.Data;

/**
 * 基础类型的包装一层返回T
 */
@Data
public class SimpleObject {

    private Object wrapData;

    public static SimpleObject build(Object data) {
        SimpleObject t = new SimpleObject();
        t.setWrapData(data);
        return t;
    }
}
