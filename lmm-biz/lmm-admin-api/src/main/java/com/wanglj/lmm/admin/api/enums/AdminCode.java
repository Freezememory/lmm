package com.wanglj.lmm.admin.api.enums;

import lombok.Getter;

/**
 * API 接口返回状态码
 * 成功 系统操作的成功Code数值
 * 200+001 到 200 + 999
 * 成功 管理系统业务微服务操作的成功Code数值
 * 201+001 到 201 + 999
 * 成功 订单业务操作微服务的成功Code数值
 * 202+001 到 202 + 999
 * <p>
 * 失败 系统操作的失败微服务Code数值
 * 100+001 到 100 + 999
 * 失败 管理系统业务操作的微服务失败Code数值
 * 101+001 到 101 + 999
 */
@Getter
public enum AdminCode {

    //执行成功
    Success(201001, "执行成功"),
    NoData(201002, "没有数据"),

    //执行失败
    Fail(101001, "操作失败"),
    IsExistence(101002, "该关键字已经存在"),
    SysUserNotExist(101003, "该用户不存在"),
    SysUserNameExist(101004, "该用户名已经存在");

    private int code;

    private String msg;

    AdminCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
