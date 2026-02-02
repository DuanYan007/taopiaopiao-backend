package com.duanyan.taopiaopiao.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 场次响应DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "场次响应")
public class SessionResponse {

    @Schema(description = "场次ID")
    private Long id;

    @Schema(description = "演出ID")
    private Long eventId;

    @Schema(description = "演出名称")
    private String eventName;

    @Schema(description = "场次名称")
    private String sessionName;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "场馆ID")
    private Long venueId;

    @Schema(description = "场馆名称")
    private String venueName;

    @Schema(description = "馆厅名称")
    private String hallName;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "总座位数")
    private Integer totalSeats;

    @Schema(description = "可售座位数")
    private Integer availableSeats;

    @Schema(description = "已售座位数")
    private Integer soldSeats;

    @Schema(description = "锁定座位数")
    private Integer lockedSeats;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "票档列表")
    private List<TicketTierInfo> ticketTiers;

    @Schema(description = "扩展配置")
    private SessionMetadata metadata;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * 票档信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "票档信息")
    public static class TicketTierInfo {
        @Schema(description = "票档ID")
        private Long id;

        @Schema(description = "票档名称")
        private String name;

        @Schema(description = "价格")
        private Integer price;

        @Schema(description = "座位颜色")
        private String color;

        @Schema(description = "该场次分配的座位数")
        private Integer seatCount;

        @Schema(description = "该场次可售座位数")
        private Integer availableSeats;

        @Schema(description = "限购数")
        private Integer maxPurchase;

        @Schema(description = "是否启用")
        private Boolean enabled;
    }

    /**
     * 场次扩展配置
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "场次扩展配置")
    public static class SessionMetadata {
        @Schema(description = "演出时长（分钟）")
        private Integer duration;

        @Schema(description = "开售时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime saleStartTime;

        @Schema(description = "停售时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime saleEndTime;

        @Schema(description = "选座方式: online在线选座, auto系统自动分配")
        private String seatSelectionMode;

        @Schema(description = "是否实名制购票")
        private Boolean requireRealName;

        @Schema(description = "每人限购1张")
        private Boolean limitOnePerPerson;

        @Schema(description = "禁止退票")
        private Boolean noRefund;

        @Schema(description = "排序权重")
        private Integer sortOrder;

        @Schema(description = "备注")
        private String remark;
    }
}
