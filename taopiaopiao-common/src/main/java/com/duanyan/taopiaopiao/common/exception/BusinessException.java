package com.duanyan.taopiaopiao.common.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author duanyan
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误提示
     */
    private final String message;

    /**
     * 错误明细
     */
    private final String detailMessage;

    public BusinessException() {
        this(500, "业务异常");
    }

    public BusinessException(String message) {
        this(500, message);
    }

    public BusinessException(Integer code, String message) {
        this(code, message, null);
    }

    public BusinessException(Integer code, String message, String detailMessage) {
        super(message);
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
        this.message = message;
        this.detailMessage = cause.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
