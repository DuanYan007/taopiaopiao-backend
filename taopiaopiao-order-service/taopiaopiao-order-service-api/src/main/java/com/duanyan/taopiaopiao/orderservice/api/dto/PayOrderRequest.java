package com.duanyan.taopiaopiao.orderservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 支付订单请求
 */
@Data
@Schema(description = "支付订单请求")
public class PayOrderRequest {

    @NotBlank(message = "订单号不能为空")
    @Schema(description = "订单号", required = true)
    private String orderNo;
}
