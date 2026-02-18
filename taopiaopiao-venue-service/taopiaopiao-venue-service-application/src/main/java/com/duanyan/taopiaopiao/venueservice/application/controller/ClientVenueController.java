package com.duanyan.taopiaopiao.venueservice.application.controller;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenuePageResponse;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueQueryRequest;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueResponse;
import com.duanyan.taopiaopiao.venueservice.application.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 场馆客户端控制器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/client/venues")
@RequiredArgsConstructor
@Tag(name = "场馆-客户端", description = "客户端场馆查询接口")
public class ClientVenueController {

    private final VenueService venueService;

    /**
     * 分页查询场馆列表（客户端）
     */
    @GetMapping
    @Operation(summary = "分页查询场馆列表（客户端）")
    public Result<VenuePageResponse> getVenuePage(VenueQueryRequest request) {
        VenuePageResponse response = venueService.getVenuePage(request);
        return Result.success(response);
    }

    /**
     * 根据ID查询场馆详情（客户端）
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询场馆详情（客户端）")
    public Result<VenueResponse> getVenueById(
            @Parameter(description = "场馆ID", required = true)
            @PathVariable Long id) {
        VenueResponse response = venueService.getVenueById(id);
        return Result.success(response);
    }
}
