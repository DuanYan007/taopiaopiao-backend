package com.duanyan.taopiaopiao.venueservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 场馆响应DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "场馆响应")
public class VenueResponse {

    @Schema(description = "场馆ID")
    private Long id;

    @Schema(description = "场馆名称")
    private String name;

    @Schema(description = "所在城市")
    private String city;

    @Schema(description = "所在区域")
    private String district;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "容纳人数")
    private Integer capacity;

    @Schema(description = "设施数组")
    private List<String> facilities;

    @Schema(description = "场馆介绍")
    private String description;

    @Schema(description = "场馆图片URL列表")
    private List<String> images;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
