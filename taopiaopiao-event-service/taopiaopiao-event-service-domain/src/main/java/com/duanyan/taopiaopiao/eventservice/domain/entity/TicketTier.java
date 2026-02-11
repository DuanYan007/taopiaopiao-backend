package com.duanyan.taopiaopiao.eventservice.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 票档实体
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@TableName("ticket_tiers")
public class TicketTier {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属演出ID
     */
    private Long eventId;

    /**
     * 票档名称
     */
    private String name;

    /**
     * 票价
     */
    private BigDecimal price;

    /**
     * 座位图颜色 #FF5722
     */
    private String color;

    /**
     * 票档说明
     */
    private String description;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 每单限购
     */
    private Integer maxPurchase;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 扩展字段(JSON)
     */
    private String metadata;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
