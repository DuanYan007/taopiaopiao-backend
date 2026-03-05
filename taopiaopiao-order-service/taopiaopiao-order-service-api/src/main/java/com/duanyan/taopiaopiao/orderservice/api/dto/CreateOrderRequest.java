package com.duanyan.taopiaopiao.orderservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单请求
 */
@Data
@Schema(description = "创建订单请求")
public class CreateOrderRequest {

    @NotNull(message = "场次ID不能为空")
    @Schema(description = "场次ID", required = true)
    private Long sessionId;

    @Schema(description = "演出ID")
    private Long eventId;

    @NotEmpty(message = "座位ID不能为空")
    @Schema(description = "座位ID列表", required = true)
    private List<String> seatIds;

    @NotEmpty(message = "座位详情不能为空")
    @Schema(description = "座位详情列表", required = true)
    private List<SeatDetail> seatDetails;

    @NotNull(message = "总金额不能为空")
    @Schema(description = "订单总金额", required = true)
    private BigDecimal totalAmount;
}
