package com.duanyan.taopiaopiao.seatternplateservice.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 座位模板票档关联实体
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@TableName("seat_template_tier_ref")
public class SeatTemplateTierRef {

    /**
     * 关联ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 票档ID
     */
    private Long tierId;

    /**
     * 区域编码
     */
    private String areaCode;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 该区域价格
     */
    private BigDecimal price;

    /**
     * 该区域座位数
     */
    private Integer seatCount;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
