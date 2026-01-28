package com.duanyan.taopiaopiao.api.exception;

import com.duanyan.taopiaopiao.common.exception.BusinessException;
import com.duanyan.taopiaopiao.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 静态资源未找到异常 (忽略，不打印错误日志)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNoResourceFoundException(NoResourceFoundException e) {
        // 不打印日志，这是正常的404请求
        return Result.fail(404, "资源不存在");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        if (e.getDetailMessage() != null) {
            log.error("异常详情: {}", e.getDetailMessage());
        }
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常 (RequestBody)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数校验异常: {}", e.getMessage());
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.fail(400, errorMsg);
    }

    /**
     * 参数绑定异常 (ModelAttribute)
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        log.error("参数绑定异常: {}", e.getMessage());
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.fail(400, errorMsg);
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return Result.fail(500, "系统繁忙,请稍后重试");
    }
}
