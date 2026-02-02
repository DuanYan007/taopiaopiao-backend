package com.duanyan.taopiaopiao.domain.dto;

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
 * 场次更新请求DTO
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "场次更新请求")
public class SessionUpdateRequest {

    @NotNull(message = "演出ID不能为空")
    @Schema(description = "演出ID", example = "18", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long eventId;

    @NotBlank(message = "场次名称不能为空")
    @Schema(description = "场次名称", example = "2025-03-15 19:30场", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sessionName;

    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间", example = "2025-03-15T19:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间", example = "2025-03-15T22:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    @NotNull(message = "场馆ID不能为空")
    @Schema(description = "场馆ID", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long venueId;

    @Schema(description = "馆厅名称", example = "主体育场")
    private String hallName;

    @Schema(description = "详细地址", example = "北京市朝阳区国家体育场南路1号")
    private String address;

    @NotNull(message = "总座位数不能为空")
    @Min(value = 1, message = "总座位数必须大于0")
    @Schema(description = "总座位数", example = "8560", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalSeats;

    @NotNull(message = "可售座位数不能为空")
    @Min(value = 0, message = "可售座位数不能小于0")
    @Schema(description = "可售座位数", example = "8560", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer availableSeats;

    @NotEmpty(message = "票档配置不能为空")
    @Valid
    @Schema(description = "票档配置", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SessionCreateRequest.TicketTierConfigRequest> ticketTierConfig;

    @Valid
    @Schema(description = "扩展配置")
    private SessionCreateRequest.SessionMetadataRequest metadata;

    @NotBlank(message = "状态不能为空")
    @Schema(description = "状态", example = "not_started", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;
}
