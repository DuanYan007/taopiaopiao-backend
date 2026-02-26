package com.duanyan.taopiaopiao.seatternplateservice.application.controller;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatLayoutResponse;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplateResponse;
import com.duanyan.taopiaopiao.seatternplateservice.application.service.SeatTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 座位模板客户端控制器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/client/seat-templates")
@RequiredArgsConstructor
@Tag(name = "座位模板-客户端", description = "客户端座位模板查询接口")
public class ClientSeatTemplateController {

    private final SeatTemplateService seatTemplateService;

    /**
     * 根据场次ID获取座位布局
     * 注意：这里需要通过session-service获取场次信息，然后查询对应的座位模板
     * 目前先提供根据模板ID查询的接口
     */
    @GetMapping("/{templateId}/layout")
    @Operation(summary = "获取座位布局", description = "根据模板ID获取座位布局数据")
    public Result<SeatLayoutResponse> getLayoutByTemplateId(
            @Parameter(description = "模板ID", required = true)
            @PathVariable Long templateId) {
        SeatTemplateResponse template = seatTemplateService.getTemplateById(templateId);

        SeatLayoutResponse response = new SeatLayoutResponse();
        response.setTemplateId(template.getId());
        response.setTemplateName(template.getName());
        response.setVenueId(template.getVenueId());
        response.setLayoutType(template.getLayoutType());
        response.setLayoutData(template.getLayoutData());

        return Result.success(response);
    }
}
