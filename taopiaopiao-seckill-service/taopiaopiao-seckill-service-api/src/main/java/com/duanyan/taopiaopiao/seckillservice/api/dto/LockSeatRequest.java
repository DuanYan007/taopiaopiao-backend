package com.duanyan.taopiaopiao.seckillservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 锁座请求
 */
@Data
@Schema(description = "锁座请求")
public class LockSeatRequest {

    @Schema(description = "场次ID", required = true)
    @NotNull(message = "场次ID不能为空")
    private Long sessionId;

    @Schema(description = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "座位ID列表，格式: [\"1:1\",\"1:2\"]", required = true)
    @NotEmpty(message = "座位ID不能为空")
    private List<String> seatIds;

    @Schema(description = "锁定时长（秒），默认300秒")
    private Integer expireSeconds = 300;
}
