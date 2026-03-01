package com.duanyan.taopiaopiao.seckillservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 锁座状态枚举
 */
@Getter
@AllArgsConstructor
public enum LockStatus {
    LOCKED(0, "已锁定"),
    PURCHASED(1, "已购买"),
    RELEASED(2, "已释放");

    private final Integer code;
    private final String desc;
}
