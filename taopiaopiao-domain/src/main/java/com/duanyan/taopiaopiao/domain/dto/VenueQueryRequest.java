package com.duanyan.taopiaopiao.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 场馆查询请求DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "场馆查询请求")
public class VenueQueryRequest {

    @Schema(description = "场馆名称（模糊搜索）")
    private String keyword;

    @Schema(description = "城市筛选")
    private String city;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
