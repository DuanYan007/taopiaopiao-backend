package com.duanyan.taopiaopiao.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 座位实体
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@TableName("seats")
public class Seat {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属场次ID
     */
    private Long sessionId;

    /**
     * 票档ID
     */
    private Long ticketTierId;

    /**
     * 行号
     */
    private String seatRow;

    /**
     * 列号
     */
    private String seatColumn;

    /**
     * 完整座位号
     */
    private String seatNumber;

    /**
     * 区域
     */
    private String area;

    /**
     * 座位价格
     */
    private BigDecimal price;

    /**
     * 状态: available, sold, locked, unavailable
     */
    private String status;

    /**
     * 锁定者
     */
    private Long lockedBy;

    /**
     * 锁定过期时间
     */
    private LocalDateTime lockedUntil;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 扩展字段(JSON)
     */
    private String metadata;

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
}
