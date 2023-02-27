package com.wanglj.lmm.common.base.util;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应信息主体
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    private static int getCode(Enum en) {
        Class<? extends Enum> tClass = en.getClass();
        Integer code = 0;
        try {
            code = (Integer) tClass.getMethod("getCode").invoke(en);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    private static String getMsg(Enum en) {
        Class<? extends Enum> tClass = en.getClass();
        String msg = "enum error";
        try {
            msg = (String) tClass.getMethod("getMsg").invoke(en);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static <T> R<T> ok(Enum en) {
        return restResult(null, getCode(en), getMsg(en));
    }

    public static <T> R<T> ok(Enum en, T data) {
        return restResult(data, getCode(en), getMsg(en));
    }

    public static <T> R<T> ok(int code, String msg, T data) {
        return restResult(data, code, msg);
    }

    public static <T> R<T> fail(Enum en) {
        return restResult(null, getCode(en), getMsg(en));
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, 500, msg);
    }

    //public static <T> R<T> fail(Enum en, T data) {
    //    return restResult(data, getCode(en), getMsg(en));
    //}

    public static <T> R<T> fail(Integer code,String msg) {
        return restResult(null, code, msg);
    }

    public static <T> R<T> fail(int code, String msg, T data) {
        return restResult(data, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
