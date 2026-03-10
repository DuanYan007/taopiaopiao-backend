package com.duanyan.taopiaopiao.sessionservice.application.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 标记座位已售出请求
 */
@Data
@Schema(description = "标记座位已售出请求")
public class MarkSeatsSoldRequest {

    @NotNull(message = "场次ID不能为空")
    @Schema(description = "场次ID", required = true)
    private Long sessionId;

    @NotEmpty(message = "座位ID不能为空")
    @Schema(description = "座位ID列表", required = true)
    private List<String> seatIds;

    @Schema(description = "订单号")
    private String orderNo;
}
