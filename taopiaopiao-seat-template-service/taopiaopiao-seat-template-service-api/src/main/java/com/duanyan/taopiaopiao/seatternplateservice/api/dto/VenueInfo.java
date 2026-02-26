package com.duanyan.taopiaopiao.seatternplateservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 场馆信息DTO（用于接收venue-service的返回数据）
 *
 * 注意：此DTO的字段定义与 venue-service 的 VenueResponse 保持兼容，
 * 以便 Feign 调用时能正确反序列化
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "场馆信息")
public class VenueInfo {

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

    @Schema(description = "设施数组(JSON)")
    private String facilities;

    @Schema(description = "场馆介绍")
    private String description;

    @Schema(description = "场馆图片URL列表(JSON)")
    private String images;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
