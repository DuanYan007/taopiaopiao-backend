package com.duanyan.taopiaopiao.seatternplateservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * 座位模板创建请求
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "座位模板创建请求")
public class SeatTemplateCreateRequest {

    @NotBlank(message = "模板名称不能为空")
    @Schema(description = "模板名称", example = "大剧院标准模板", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "场馆ID不能为空")
    @Schema(description = "关联场馆ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long venueId;

    @NotBlank(message = "模板编码不能为空")
    @Schema(description = "模板编码(唯一标识)", example = "THEATER_STD_001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String templateCode;

    @NotNull(message = "总行数不能为空")
    @Positive(message = "总行数必须大于0")
    @Schema(description = "总行数", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalRows;

    @NotNull(message = "总座位数不能为空")
    @Positive(message = "总座位数必须大于0")
    @Schema(description = "总座位数", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalSeats;

    @NotNull(message = "布局类型不能为空")
    @Schema(description = "布局类型: 1=普通, 2=VIP分区, 3=混合, 4=自定义", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer layoutType;

    @NotBlank(message = "布局数据不能为空")
    @Schema(description = "座位布局数据(JSON字符串)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String layoutData;
}
