package com.wanglj.lmm.common.base.exception;

/**
 * 异常处理
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /** 异常码 */
    private final int code;

    public BaseException() {
        super("系统异常");
        this.code = 100000;
    }

/*    public BaseException(ResultEnum resultEnum) {
        super(resultEnum.getDesc());
        this.code = resultEnum.getCode();
    }
    
    public BaseException(IErrorCode iErrorCode) {
        super(iErrorCode.getErrorMessage());
        this.code = iErrorCode.getErrorCode();
    }*/
    
    public BaseException(String message) {
        super(message);
        this.code = 100000;
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
