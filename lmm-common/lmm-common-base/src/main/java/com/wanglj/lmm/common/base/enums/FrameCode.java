package com.wanglj.lmm.common.base.enums;

import lombok.Getter;

/**
 * API 接口返回状态码
 * 成功 系统框架作的成功Code数值
 * 200+001 到 200 + 999
 * <p>
 * 失败 系统框架的失败微服务Code数值
 * 100+001 到 100 + 999
 */
@Getter
public enum FrameCode {
    Success(200001, "操作成功"),

    SystemInnerError(100001, "系统内部错误"),
    AuthenticationFailed(100002, "认证失败无法访问系统资源"),
    LoginFailed(100003, "登录失败"),
    AccountOrPwdFailed(100004, "账号或密码错误");

    private int code;

    private String msg;

    FrameCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
