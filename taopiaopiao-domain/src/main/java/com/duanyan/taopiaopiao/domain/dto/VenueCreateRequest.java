package com.duanyan.taopiaopiao.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 场馆创建请求DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "场馆创建请求")
public class VenueCreateRequest {

    @NotBlank(message = "场馆名称不能为空")
    @Schema(description = "场馆名称", example = "上海体育场", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "城市不能为空")
    @Schema(description = "所在城市", example = "上海", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Schema(description = "所在区域", example = "徐汇区")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Schema(description = "详细地址", example = "上海市徐汇区天钥桥路666号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @Schema(description = "纬度", example = "31.123456")
    private BigDecimal latitude;

    @Schema(description = "经度", example = "121.123456")
    private BigDecimal longitude;

    @NotNull(message = "容纳人数不能为空")
    @Schema(description = "容纳人数", example = "56000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer capacity;

    @Schema(description = "设施数组", example = "[\"停车场\", \"地铁\", \"公交\"]")
    private List<String> facilities;

    @Schema(description = "场馆介绍")
    private String description;

    @Schema(description = "场馆图片URL（逗号分隔）", example = "url1,url2,url3")
    private String images;
}
