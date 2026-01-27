package com.duanyan.taopiaopiao.common.response;

import com.duanyan.taopiaopiao.common.constant.Constants;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果封装
 *
 * @param <T> 数据类型
 * @author duanyan
 * @since 1.0.0
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return new Result<>(Constants.SUCCESS, "操作成功");
    }

    /**
     * 成功返回结果
     *
     * @param msg 返回消息
     */
    public static <T> Result<T> success(String msg) {
        return new Result<>(Constants.SUCCESS, msg);
    }

    /**
     * 成功返回结果
     *
     * @param data 返回数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(Constants.SUCCESS, "操作成功", data);
    }

    /**
     * 成功返回结果
     *
     * @param msg  返回消息
     * @param data 返回数据
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(Constants.SUCCESS, msg, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> fail() {
        return new Result<>(Constants.FAIL, "操作失败");
    }

    /**
     * 失败返回结果
     *
     * @param msg 返回消息
     */
    public static <T> Result<T> fail(String msg) {
        return new Result<>(Constants.FAIL, msg);
    }

    /**
     * 失败返回结果
     *
     * @param code 状态码
     * @param msg  返回消息
     */
    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return Constants.SUCCESS == this.code;
    }
}
