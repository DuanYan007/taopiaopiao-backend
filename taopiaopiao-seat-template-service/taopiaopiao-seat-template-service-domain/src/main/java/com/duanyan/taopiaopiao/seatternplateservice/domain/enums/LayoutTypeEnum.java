package com.duanyan.taopiaopiao.seatternplateservice.domain.enums;

import lombok.Getter;

/**
 * 布局类型枚举
 *
 * @author duanyan
 * @since 1.0.0
 */
@Getter
public enum LayoutTypeEnum {

    /**
     * 普通: 全场统一价或简单分区
     */
    NORMAL(1, "普通"),

    /**
     * VIP分区: 明确的VIP/普通区域划分
     */
    VIP_PARTITION(2, "VIP分区"),

    /**
     * 混合: 多种票档混合布局
     */
    MIXED(3, "混合"),

    /**
     * 自定义: 完全自定义座位布局
     */
    CUSTOM(4, "自定义");

    private final Integer code;
    private final String desc;

    LayoutTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LayoutTypeEnum fromCode(Integer code) {
        if (code == null) {
            return NORMAL;
        }
        for (LayoutTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return NORMAL;
    }
}
