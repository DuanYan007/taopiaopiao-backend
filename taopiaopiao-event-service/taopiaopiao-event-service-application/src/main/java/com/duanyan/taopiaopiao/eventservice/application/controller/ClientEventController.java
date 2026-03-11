package com.duanyan.taopiaopiao.eventservice.application.controller;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventPageResponse;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventQueryRequest;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventResponse;
import com.duanyan.taopiaopiao.eventservice.api.dto.SessionBriefPageResponse;
import com.duanyan.taopiaopiao.eventservice.application.service.ClientEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 演出客户端控制器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/client/events")
@RequiredArgsConstructor
@Tag(name = "演出-客户端", description = "客户端演出查询接口")
public class ClientEventController {

    private final ClientEventService clientEventService;

    /**
     * 分页查询演出列表（客户端）
     */
    @GetMapping
    @Operation(summary = "分页查询演出列表（客户端）")
    public Result<EventPageResponse> getEventPage(EventQueryRequest request) {
        EventPageResponse response = clientEventService.getEventPage(request);
        return Result.success(response);
    }

    /**
     * 根据ID查询演出详情（客户端）
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询演出详情（客户端）")
    public Result<EventResponse> getEventById(
            @Parameter(description = "演出ID", required = true)
            @PathVariable Long id) {
        EventResponse response = clientEventService.getEventById(id);
        return Result.success(response);
    }

    /**
     * 查询演出的场次列表（客户端）
     */
    @GetMapping("/{eventId}/sessions")
    @Operation(summary = "查询演出的场次列表（客户端）")
    public Result<SessionBriefPageResponse> getEventSessions(
            @Parameter(description = "演出ID", required = true)
            @PathVariable Long eventId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        SessionBriefPageResponse response = clientEventService.getEventSessions(eventId, status, page, pageSize);
        return Result.success(response);
    }
}
