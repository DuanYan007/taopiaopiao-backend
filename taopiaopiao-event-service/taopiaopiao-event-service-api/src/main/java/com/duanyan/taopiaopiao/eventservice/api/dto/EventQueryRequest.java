package com.duanyan.taopiaopiao.eventservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 演出查询请求DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "演出查询请求")
public class EventQueryRequest {

    @Schema(description = "关键词（演出名称、艺人）")
    private String keyword;

    @Schema(description = "城市筛选")
    private String city;

    @Schema(description = "类型筛选")
    private String type;

    @Schema(description = "状态筛选")
    private String status;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
