package com.wanglj.lmm.common.security.config;

import cn.hutool.core.exceptions.ValidateException;
import com.wanglj.lmm.common.base.exception.BaseException;
import com.wanglj.lmm.common.base.util.R;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.wanglj.lmm.common.base.enums.FrameCode.*;


/**
 * @description: 自定义异常处理
 * @author: Wanglj
 * @date: 2023/2/17 21:51
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 系统自定义异常
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public R<String> baseExceptionHandler(BaseException exception) {
        return R.fail(exception.getCode(),exception.getMessage());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(value = ValidateException.class)
    @ResponseBody
    public R<String> validateExceptionHandler(ValidateException exception) {
        return R.fail(ValidateFailed.getCode(),exception.getMessage());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public R<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        return R.fail(ValidateFailed.getCode(), exception.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R<String> exceptionHandler(Exception exception) {
        return R.fail(OtherFailed.getCode(),exception.getMessage());
    }

}
