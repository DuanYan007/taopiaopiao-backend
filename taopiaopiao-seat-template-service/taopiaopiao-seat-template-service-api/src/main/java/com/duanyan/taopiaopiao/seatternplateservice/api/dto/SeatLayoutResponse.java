package com.duanyan.taopiaopiao.seatternplateservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 座位布局响应（客户端）
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "座位布局响应")
public class SeatLayoutResponse {

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "场馆ID")
    private Long venueId;

    @Schema(description = "布局类型")
    private Integer layoutType;

    @Schema(description = "座位布局数据")
    private String layoutData;
}
