package com.duanyan.taopiaopiao.common.redis.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 座位状态枚举
 *
 * @author duanyan
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum SeatStatus {

    /**
     * 可选
     */
    AVAILABLE(0, "可选"),

    /**
     * 已锁定
     */
    LOCKED(1, "已锁定"),

    /**
     * 已售出
     */
    SOLD(2, "已售出");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 根据状态码获取枚举
     */
    public static SeatStatus fromCode(int code) {
        for (SeatStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否可选
     */
    public boolean isAvailable() {
        return this == AVAILABLE;
    }

    /**
     * 判断是否已锁定
     */
    public boolean isLocked() {
        return this == LOCKED;
    }

    /**
     * 判断是否已售出
     */
    public boolean isSold() {
        return this == SOLD;
    }
}
