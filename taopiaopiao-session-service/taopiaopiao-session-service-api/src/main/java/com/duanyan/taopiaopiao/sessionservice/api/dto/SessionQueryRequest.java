package com.duanyan.taopiaopiao.sessionservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 场次查询请求DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "场次查询请求")
public class SessionQueryRequest {

    @Schema(description = "关键词", example = "周杰伦")
    private String keyword;

    @Schema(description = "演出ID", example = "18")
    private Long eventId;

    @Schema(description = "场馆ID", example = "2")
    private Long venueId;

    @Schema(description = "状态", example = "on_sale")
    private String status;

    @Schema(description = "页码", example = "1")
    @Builder.Default
    private Integer page = 1;

    @Schema(description = "每页条数", example = "10")
    @Builder.Default
    private Integer pageSize = 10;
}
