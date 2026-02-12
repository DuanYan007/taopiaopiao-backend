package com.duanyan.taopiaopiao.sessionservice.application.controller;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionCreateRequest;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionPageResponse;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionQueryRequest;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionResponse;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionUpdateRequest;
import com.duanyan.taopiaopiao.sessionservice.application.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 场次管理控制器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/sessions")
@RequiredArgsConstructor
@Tag(name = "场次管理", description = "场次增删改查接口")
public class SessionController {

    private final SessionService sessionService;

    /**
     * 分页查询场次列表
     */
    @GetMapping
    @Operation(summary = "分页查询场次列表")
    public Result<SessionPageResponse> getSessionPage(SessionQueryRequest request) {
        SessionPageResponse response = sessionService.getSessionPage(request);
        return Result.success(response);
    }

    /**
     * 根据ID查询场次详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询场次详情")
    public Result<SessionResponse> getSessionById(
            @Parameter(description = "场次ID", required = true)
            @PathVariable Long id) {
        SessionResponse response = sessionService.getSessionById(id);
        return Result.success(response);
    }

    /**
     * 创建场次
     */
    @PostMapping
    @Operation(summary = "创建场次")
    public Result<Long> createSession(@Valid @RequestBody SessionCreateRequest request) {
        Long sessionId = sessionService.createSession(request);
        return Result.success(sessionId);
    }

    /**
     * 更新场次
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新场次")
    public Result<Void> updateSession(
            @Parameter(description = "场次ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody SessionUpdateRequest request) {
        sessionService.updateSession(id, request);
        return Result.success();
    }

    /**
     * 删除场次
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除场次")
    public Result<Void> deleteSession(
            @Parameter(description = "场次ID", required = true)
            @PathVariable Long id) {
        sessionService.deleteSession(id);
        return Result.success();
    }

    /**
     * 更新场次状态
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新场次状态")
    public Result<Void> updateSessionStatus(
            @Parameter(description = "场次ID", required = true)
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        sessionService.updateSessionStatus(id, request.getStatus());
        return Result.success();
    }

    /**
     * 状态更新请求
     */
    @lombok.Data
    @io.swagger.v3.oas.annotations.media.Schema(description = "状态更新请求")
    public static class StatusUpdateRequest {
        @io.swagger.v3.oas.annotations.media.Schema(description = "状态", example = "on_sale")
        private String status;
    }
}
