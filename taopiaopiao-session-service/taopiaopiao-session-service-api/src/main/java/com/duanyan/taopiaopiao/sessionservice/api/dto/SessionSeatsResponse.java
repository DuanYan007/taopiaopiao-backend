package com.duanyan.taopiaopiao.sessionservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 场次座位列表响应DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "场次座位列表响应")
public class SessionSeatsResponse {

    @Schema(description = "场次ID")
    private Long sessionId;

    @Schema(description = "座位列表")
    private List<SeatResponse> seats;
}
