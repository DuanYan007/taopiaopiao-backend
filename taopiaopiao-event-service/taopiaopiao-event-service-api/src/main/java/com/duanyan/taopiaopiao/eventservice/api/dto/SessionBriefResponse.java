package com.duanyan.taopiaopiao.eventservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 场次简略响应DTO（用于演出服务返回场次信息）
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "场次简略响应")
public class SessionBriefResponse {

    @Schema(description = "场次ID")
    private Long id;

    @Schema(description = "演出ID")
    private Long eventId;

    @Schema(description = "场次名称")
    private String sessionName;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "座位模板ID")
    private Long seatTemplateId;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "可售座位数")
    private Integer availableSeats;

    @Schema(description = "已售座位数")
    private Integer soldSeats;

    @Schema(description = "锁定座位数")
    private Integer lockedSeats;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "扩展配置")
    private SessionMetadata metadata;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

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

        @Schema(description = "选座方式")
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
