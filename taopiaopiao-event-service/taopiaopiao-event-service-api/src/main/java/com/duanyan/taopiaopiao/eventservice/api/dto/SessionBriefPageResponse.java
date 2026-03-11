package com.duanyan.taopiaopiao.eventservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 场次分页响应DTO（用于演出服务返回场次列表）
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "场次分页响应")
public class SessionBriefPageResponse {

    @Schema(description = "场次列表")
    private List<SessionBriefResponse> list;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "当前页码")
    private Integer page;

    @Schema(description = "每页条数")
    private Integer pageSize;

    @Schema(description = "总页数")
    private Integer totalPages;
}
