package com.duanyan.taopiaopiao.seatternplateservice.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 座位模板实体
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@TableName("seat_templates")
public class SeatTemplate {

    /**
     * 模板ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 关联场馆ID
     */
    private Long venueId;

    /**
     * 模板编码(唯一标识)
     */
    private String templateCode;

    /**
     * 总行数
     */
    private Integer totalRows;

    /**
     * 总座位数
     */
    private Integer totalSeats;

    /**
     * 布局类型: 1=普通, 2=VIP分区, 3=混合, 4=自定义
     */
    private Integer layoutType;

    /**
     * 座位布局数据(JSON)
     */
    private String layoutData;

    /**
     * 状态: 0=禁用, 1=启用
     */
    private Integer status;

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
     * 逻辑删除: 0=未删除, 1=已删除
     */
    @TableLogic
    private Integer deleted;
}
