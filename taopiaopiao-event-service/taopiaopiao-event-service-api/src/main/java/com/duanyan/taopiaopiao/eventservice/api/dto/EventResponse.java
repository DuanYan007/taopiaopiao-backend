package com.duanyan.taopiaopiao.eventservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 演出响应DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "演出响应")
public class EventResponse {

    @Schema(description = "演出ID")
    private Long id;

    @Schema(description = "演出名称")
    private String name;

    @Schema(description = "演出类型")
    private String type;

    @Schema(description = "艺人/主办方")
    private String artist;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "副标题")
    private String subtitle;

    @Schema(description = "演出开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventStartDate;

    @Schema(description = "演出结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventEndDate;

    @Schema(description = "演出时长（分钟）")
    private Integer duration;

    @Schema(description = "演出简介")
    private String description;

    @Schema(description = "封面图片URL")
    private String coverImage;

    @Schema(description = "图片URL列表")
    private List<String> images;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "开售时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime saleStartTime;

    @Schema(description = "停售时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime saleEndTime;

    @Schema(description = "标签列表")
    private List<String> tags;

    @Schema(description = "温馨提示")
    private String tips;

    @Schema(description = "退换票政策")
    private String refundPolicy;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
