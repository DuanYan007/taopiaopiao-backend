package com.duanyan.taopiaopiao.seatternplateservice.application.controller;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplateCreateRequest;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplatePageResponse;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplateQueryRequest;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplateResponse;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplateUpdateRequest;
import com.duanyan.taopiaopiao.seatternplateservice.application.service.SeatTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 座位模板管理控制器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/seat-templates")
@RequiredArgsConstructor
@Tag(name = "座位模板管理", description = "座位模板增删改查接口")
public class SeatTemplateController {

    private final SeatTemplateService seatTemplateService;

    /**
     * 分页查询座位模板列表
     */
    @GetMapping
    @Operation(summary = "分页查询座位模板列表")
    public Result<SeatTemplatePageResponse> getTemplatePage(SeatTemplateQueryRequest request) {
        SeatTemplatePageResponse response = seatTemplateService.getTemplatePage(request);
        return Result.success(response);
    }

    /**
     * 根据ID查询座位模板详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询座位模板详情")
    public Result<SeatTemplateResponse> getTemplateById(
            @Parameter(description = "模板ID", required = true)
            @PathVariable Long id) {
        SeatTemplateResponse response = seatTemplateService.getTemplateById(id);
        return Result.success(response);
    }

    /**
     * 创建座位模板
     */
    @PostMapping
    @Operation(summary = "创建座位模板")
    public Result<Long> createTemplate(@Valid @RequestBody SeatTemplateCreateRequest request) {
        Long templateId = seatTemplateService.createTemplate(request);
        return Result.success(templateId);
    }

    /**
     * 更新座位模板
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新座位模板")
    public Result<Void> updateTemplate(
            @Parameter(description = "模板ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody SeatTemplateUpdateRequest request) {
        seatTemplateService.updateTemplate(id, request);
        return Result.success();
    }

    /**
     * 删除座位模板
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除座位模板")
    public Result<Void> deleteTemplate(
            @Parameter(description = "模板ID", required = true)
            @PathVariable Long id) {
        seatTemplateService.deleteTemplate(id);
        return Result.success();
    }

    /**
     * 根据场馆ID查询模板列表
     */
    @GetMapping("/venue/{venueId}")
    @Operation(summary = "根据场馆ID查询模板列表")
    public Result<java.util.List<SeatTemplateResponse>> listByVenueId(
            @Parameter(description = "场馆ID", required = true)
            @PathVariable Long venueId) {
        java.util.List<SeatTemplateResponse> response = seatTemplateService.listByVenueId(venueId);
        return Result.success(response);
    }
}
