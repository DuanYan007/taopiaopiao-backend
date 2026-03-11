package com.duanyan.taopiaopiao.seatternplateservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * 座位模板更新请求
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "座位模板更新请求")
public class SeatTemplateUpdateRequest {

    @Schema(description = "模板名称", example = "大剧院标准模板V2")
    private String name;

    @Schema(description = "总行数", example = "12")
    @Positive(message = "总行数必须大于0")
    private Integer totalRows;

    @Schema(description = "总座位数", example = "240")
    @Positive(message = "总座位数必须大于0")
    private Integer totalSeats;

    @Schema(description = "布局类型: 1=普通, 2=VIP分区, 3=混合, 4=自定义", example = "2")
    private Integer layoutType;

    @Schema(description = "座位布局数据(JSON字符串)")
    private String layoutData;

    @Schema(description = "状态: 0=禁用, 1=启用", example = "1")
    private Integer status;
}
