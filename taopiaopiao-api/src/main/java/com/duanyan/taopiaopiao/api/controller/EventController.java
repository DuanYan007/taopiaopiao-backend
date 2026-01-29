package com.duanyan.taopiaopiao.api.controller;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.domain.dto.EventCreateRequest;
import com.duanyan.taopiaopiao.domain.dto.EventPageResponse;
import com.duanyan.taopiaopiao.domain.dto.EventQueryRequest;
import com.duanyan.taopiaopiao.domain.dto.EventResponse;
import com.duanyan.taopiaopiao.domain.dto.EventUpdateRequest;
import com.duanyan.taopiaopiao.application.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 演出管理控制器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Tag(name = "演出管理", description = "演出增删改查接口")
@SecurityRequirement(name = "Authorization")
public class EventController {

    private final EventService eventService;

    /**
     * 分页查询演出列表
     */
    @GetMapping
    @Operation(summary = "分页查询演出列表")
    public Result<EventPageResponse> getEventPage(EventQueryRequest request) {
        EventPageResponse response = eventService.getEventPage(request);
        return Result.success(response);
    }

    /**
     * 根据ID查询演出详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询演出详情")
    public Result<EventResponse> getEventById(
            @Parameter(description = "演出ID", required = true)
            @PathVariable Long id) {
        EventResponse response = eventService.getEventById(id);
        return Result.success(response);
    }

    /**
     * 创建演出
     */
    @PostMapping
    @Operation(summary = "创建演出")
    public Result<Long> createEvent(@Valid @RequestBody EventCreateRequest request) {
        Long eventId = eventService.createEvent(request);
        return Result.success(eventId);
    }

    /**
     * 更新演出
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新演出")
    public Result<Void> updateEvent(
            @Parameter(description = "演出ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody EventUpdateRequest request) {
        eventService.updateEvent(id, request);
        return Result.success();
    }

    /**
     * 删除演出
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除演出")
    public Result<Void> deleteEvent(
            @Parameter(description = "演出ID", required = true)
            @PathVariable Long id) {
        eventService.deleteEvent(id);
        return Result.success();
    }

    /**
     * 更新演出状态
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新演出状态")
    public Result<Void> updateEventStatus(
            @Parameter(description = "演出ID", required = true)
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        eventService.updateEventStatus(id, request.getStatus());
        return Result.success();
    }

    /**
     * 状态更新请求
     */
    @lombok.Data
    @io.swagger.v3.oas.annotations.media.Schema(description = "状态更新请求")
    public static class StatusUpdateRequest {
        @io.swagger.v3.oas.annotations.media.Schema(description = "状态", example = "off_sale")
        private String status;
    }
}
