package com.duanyan.taopiaopiao.orderservice.application.client.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 确认购买请求
 */
@Data
public class ConfirmPurchaseRequest {

    @NotNull(message = "场次ID不能为空")
    private Long sessionId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotEmpty(message = "座位ID不能为空")
    private List<String> seatIds;
}
