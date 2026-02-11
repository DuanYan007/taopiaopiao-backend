package com.duanyan.taopiaopiao.eventservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 演出创建请求DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Schema(description = "演出创建请求")
public class EventCreateRequest {

    @NotBlank(message = "演出名称不能为空")
    @Schema(description = "演出名称", example = "周杰伦2025嘉年华世界巡回演唱会-上海站", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "演出类型不能为空")
    @Schema(description = "演出类型", example = "concert", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @NotBlank(message = "艺人/主办方不能为空")
    @Schema(description = "艺人/主办方", example = "周杰伦", requiredMode = Schema.RequiredMode.REQUIRED)
    private String artist;

    @NotBlank(message = "城市不能为空")
    @Schema(description = "城市", example = "上海", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @NotNull(message = "场馆ID不能为空")
    @Schema(description = "默认场馆ID", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long venueId;

    @Schema(description = "副标题", example = "嘉年华世界巡回演唱会")
    private String subtitle;

    @Schema(description = "演出开始日期", example = "2025-03-15")
    private LocalDate eventStartDate;

    @Schema(description = "演出结束日期", example = "2025-03-15")
    private LocalDate eventEndDate;

    @Schema(description = "演出时长（分钟）", example = "180")
    private Integer duration;

    @NotNull(message = "开售时间不能为空")
    @Schema(description = "开售时间", example = "2025-02-01T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime saleStartTime;

    @Schema(description = "停售时间", example = "2025-03-15T19:30:00")
    private LocalDateTime saleEndTime;

    @Schema(description = "封面图片URL", example = "https://example.com/image.jpg")
    private String coverImage;

    @Schema(description = "演出图片URL（逗号分隔）", example = "url1,url2,url3")
    private String images;

    @Schema(description = "演出简介")
    private String description;

    @Schema(description = "票档列表")
    private List<TicketTierDTO> ticketTiers;

    @Schema(description = "温馨提示", example = "儿童入场提示")
    private String tips;

    @Schema(description = "退换票政策", example = "开演前7天可退票")
    private String refundPolicy;

    @Schema(description = "状态", example = "draft")
    private String status = "draft";

    @Schema(description = "标签数组", example = "[\"recommended\", \"hot\"]")
    private List<String> tags;

    /**
     * 票档DTO
     */
    @Data
    @Schema(description = "票档信息")
    public static class TicketTierDTO {
        @Schema(description = "票档名称", example = "VIP")
        private String name;

        @Schema(description = "价格", example = "2580")
        private Integer price;

        @Schema(description = "座位颜色（十六进制）", example = "#FF5722")
        private String color;

        @Schema(description = "每人限购数量", example = "4")
        private Integer maxPurchase;

        @Schema(description = "票档说明", example = "含周边礼包")
        private String description;
    }
}
