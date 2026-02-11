package com.duanyan.taopiaopiao.venueservice.application.controller;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueCreateRequest;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenuePageResponse;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueQueryRequest;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueResponse;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueUpdateRequest;
import com.duanyan.taopiaopiao.venueservice.application.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 场馆管理控制器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/venues")
@RequiredArgsConstructor
@Tag(name = "场馆管理", description = "场馆增删改查接口")
public class VenueController {

    private final VenueService venueService;

    /**
     * 分页查询场馆列表
     */
    @GetMapping
    @Operation(summary = "分页查询场馆列表")
    public Result<VenuePageResponse> getVenuePage(VenueQueryRequest request) {
        VenuePageResponse response = venueService.getVenuePage(request);
        return Result.success(response);
    }

    /**
     * 根据ID查询场馆详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询场馆详情")
    public Result<VenueResponse> getVenueById(
            @Parameter(description = "场馆ID", required = true)
            @PathVariable Long id) {
        VenueResponse response = venueService.getVenueById(id);
        return Result.success(response);
    }

    /**
     * 创建场馆
     */
    @PostMapping
    @Operation(summary = "创建场馆")
    public Result<Long> createVenue(@Valid @RequestBody VenueCreateRequest request) {
        Long venueId = venueService.createVenue(request);
        return Result.success(venueId);
    }

    /**
     * 更新场馆
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新场馆")
    public Result<Void> updateVenue(
            @Parameter(description = "场馆ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody VenueUpdateRequest request) {
        venueService.updateVenue(id, request);
        return Result.success();
    }

    /**
     * 删除场馆
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除场馆")
    public Result<Void> deleteVenue(
            @Parameter(description = "场馆ID", required = true)
            @PathVariable Long id) {
        venueService.deleteVenue(id);
        return Result.success();
    }
}
