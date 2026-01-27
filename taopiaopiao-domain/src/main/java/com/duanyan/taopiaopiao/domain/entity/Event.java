package com.duanyan.taopiaopiao.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 演出实体
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@TableName("events")
public class Event {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 演出名称
     */
    private String name;

    /**
     * 演出类型: concert, theatre, exhibition, sports, music, kids, dance
     */
    private String type;

    /**
     * 艺人/主办方
     */
    private String artist;

    /**
     * 城市
     */
    private String city;

    /**
     * 默认场馆ID
     */
    private Long venueId;

    /**
     * 演出开始日期
     */
    private LocalDate eventStartDate;

    /**
     * 演出结束日期
     */
    private LocalDate eventEndDate;

    /**
     * 演出简介
     */
    private String description;

    /**
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 图片数组(JSON)
     */
    private String images;

    /**
     * 状态: draft, on_sale, off_sale, sold_out
     */
    private String status;

    /**
     * 开售时间
     */
    private LocalDateTime saleStartTime;

    /**
     * 停售时间
     */
    private LocalDateTime saleEndTime;

    /**
     * 标签数组(JSON)
     */
    private String tags;

    /**
     * 扩展字段(JSON): {duration, tips, refund_policy}
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

    /**
     * 创建人ID
     */
    private Long createdBy;
}
