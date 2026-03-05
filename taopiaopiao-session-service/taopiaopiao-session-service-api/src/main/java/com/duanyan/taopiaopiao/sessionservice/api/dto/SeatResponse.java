package com.duanyan.taopiaopiao.sessionservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 座位响应DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "座位响应")
public class SeatResponse {

    @Schema(description = "座位ID")
    private Long id;

    @Schema(description = "排号")
    private String seatRow;

    @Schema(description = "列号")
    private String seatColumn;

    @Schema(description = "完整座位号")
    private String seatNumber;

    @Schema(description = "区域")
    private String area;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "状态: available-可售, sold-已售, locked-已锁定, unavailable-不可用")
    private String status;
}
