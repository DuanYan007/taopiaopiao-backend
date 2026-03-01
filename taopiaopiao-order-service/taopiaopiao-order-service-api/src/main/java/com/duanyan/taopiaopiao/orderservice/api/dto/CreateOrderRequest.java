package com.duanyan.taopiaopiao.orderservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

    @NotNull(message = "演出ID不能为空")
    @Schema(description = "演出ID", required = true)
    private Long eventId;

    @NotEmpty(message = "座位ID不能为空")
    @Schema(description = "座位ID列表", required = true)
    private List<String> seatIds;
}
