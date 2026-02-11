package com.duanyan.taopiaopiao.sessionservice.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 场次实体
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@TableName("sessions")
public class Session {

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
     * 场次名称
     */
    private String sessionName;

    /**
     * 场次开始时间
     */
    private LocalDateTime startTime;

    /**
     * 场次结束时间
     */
    private LocalDateTime endTime;

    /**
     * 场馆ID
     */
    private Long venueId;

    /**
     * 馆厅名称
     */
    private String hallName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 总座位数
     */
    private Integer totalSeats;

    /**
     * 可售座位数
     */
    private Integer availableSeats;

    /**
     * 已售座位数
     */
    private Integer soldSeats;

    /**
     * 锁定座位数
     */
    private Integer lockedSeats;

    /**
     * 状态: not_started, on_sale, sold_out, ended, off_sale
     */
    private String status;

    /**
     * 座位图配置(JSON)
     */
    private String seatMapConfig;

    /**
     * 票档配置(JSON)
     */
    private String ticketTierConfig;

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
