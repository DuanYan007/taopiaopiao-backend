package com.duanyan.taopiaopiao.seatternplateservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 座位模板分页响应
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "座位模板分页响应")
public class SeatTemplatePageResponse {

    @Schema(description = "记录总数")
    private Long total;

    @Schema(description = "当前页码")
    private Integer pageNum;

    @Schema(description = "每页大小")
    private Integer pageSize;

    @Schema(description = "模板列表")
    private List<SeatTemplateResponse> records;
}
