package com.duanyan.taopiaopiao.venueservice.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 场馆实体
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@TableName("venues")
public class Venue {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 场馆名称
     */
    private String name;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 所在区域
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 容纳人数
     */
    private Integer capacity;

    /**
     * 设施数组(JSON)
     */
    private String facilities;

    /**
     * 场馆介绍
     */
    private String description;

    /**
     * 场馆图片(JSON)
     */
    private String images;

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
