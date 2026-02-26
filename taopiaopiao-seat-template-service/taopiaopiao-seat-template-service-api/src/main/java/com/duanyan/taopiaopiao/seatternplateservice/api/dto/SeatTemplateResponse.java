package com.duanyan.taopiaopiao.seatternplateservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 座位模板响应
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "座位模板响应")
public class SeatTemplateResponse {

    @Schema(description = "模板ID", example = "1")
    private Long id;

    @Schema(description = "模板名称", example = "大剧院标准模板")
    private String name;

    @Schema(description = "关联场馆ID", example = "1")
    private Long venueId;

    @Schema(description = "场馆名称", example = "北京大剧院")
    private String venueName;

    @Schema(description = "模板编码", example = "THEATER_STD_001")
    private String templateCode;

    @Schema(description = "总行数", example = "10")
    private Integer totalRows;

    @Schema(description = "总座位数", example = "200")
    private Integer totalSeats;

    @Schema(description = "布局类型: 1=普通, 2=VIP分区, 3=混合, 4=自定义", example = "1")
    private Integer layoutType;

    @Schema(description = "布局类型名称", example = "普通")
    private String layoutTypeName;

    @Schema(description = "座位布局数据(JSON字符串)")
    private String layoutData;

    @Schema(description = "状态: 0=禁用, 1=启用", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
