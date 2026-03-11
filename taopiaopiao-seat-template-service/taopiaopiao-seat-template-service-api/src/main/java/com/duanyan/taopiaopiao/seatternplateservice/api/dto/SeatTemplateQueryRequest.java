package com.duanyan.taopiaopiao.seatternplateservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 座位模板查询请求
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "座位模板查询请求")
public class SeatTemplateQueryRequest {

    @Schema(description = "模板名称(模糊查询)", example = "大剧院")
    private String name;

    @Schema(description = "场馆ID", example = "1")
    private Long venueId;

    @Schema(description = "模板编码", example = "THEATER_STD_001")
    private String templateCode;

    @Schema(description = "布局类型", example = "1")
    private Integer layoutType;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;
}
