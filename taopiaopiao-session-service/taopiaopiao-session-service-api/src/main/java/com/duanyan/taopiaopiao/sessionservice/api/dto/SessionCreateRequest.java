package com.duanyan.taopiaopiao.sessionservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 场次创建请求DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "场次创建请求")
public class SessionCreateRequest {

    @NotNull(message = "演出ID不能为空")
    @Schema(description = "演出ID", example = "18", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long eventId;

    @NotBlank(message = "场次名称不能为空")
    @Schema(description = "场次名称", example = "2025-03-15 19:30场", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sessionName;

    @NotNull(message = "开始时间不能为空")
    @Schema(description = "开始时间", example = "2025-03-15T19:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Schema(description = "结束时间", example = "2025-03-15T22:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    @Schema(description = "详细地址")
    private String address;

    @NotNull(message = "座位模板ID不能为空")
    @Schema(description = "座位模板ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long seatTemplateId;

    @Valid
    @Schema(description = "扩展配置")
    private SessionMetadataRequest metadata;

    @NotBlank(message = "状态不能为空")
    @Schema(description = "状态", example = "not_started", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    /**
     * 场次扩展配置请求
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "场次扩展配置请求")
    public static class SessionMetadataRequest {
        @Schema(description = "演出时长（分钟）", example = "180")
        private Integer duration;

        @Schema(description = "开售时间", example = "2025-02-01T10:00:00")
        private LocalDateTime saleStartTime;

        @Schema(description = "停售时间", example = "2025-03-15T17:30:00")
        private LocalDateTime saleEndTime;

        @Schema(description = "选座方式", example = "online")
        private String seatSelectionMode;

        @Schema(description = "是否实名制购票", example = "true")
        private Boolean requireRealName;

        @Schema(description = "每人限购1张", example = "false")
        private Boolean limitOnePerPerson;

        @Schema(description = "禁止退票", example = "false")
        private Boolean noRefund;

        @Schema(description = "排序权重", example = "100")
        private Integer sortOrder;

        @Schema(description = "备注")
        private String remark;
    }
}
