package com.duanyan.taopiaopiao.orderservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 座位详情DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "座位详情")
public class SeatDetail {

    @Schema(description = "座位ID")
    private String seatId;

    @Schema(description = "区域编码")
    private String areaCode;

    @Schema(description = "区域名称")
    private String areaName;

    @Schema(description = "行号")
    private String rowNum;

    @Schema(description = "座位号")
    private String seatNum;

    @Schema(description = "座位价格")
    private BigDecimal price;
}
