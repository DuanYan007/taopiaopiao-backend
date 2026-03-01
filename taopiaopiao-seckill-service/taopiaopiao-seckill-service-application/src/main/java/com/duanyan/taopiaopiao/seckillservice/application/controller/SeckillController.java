package com.duanyan.taopiaopiao.seckillservice.application.controller;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.seckillservice.api.dto.ConfirmPurchaseRequest;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatRequest;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatResponse;
import com.duanyan.taopiaopiao.seckillservice.application.service.SeckillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "选座/秒杀", description = "座位锁定与购买接口")
@RestController
@RequestMapping("/api/seckill")
@RequiredArgsConstructor
public class SeckillController {

    private final SeckillService seckillService;

    @PostMapping("/lock")
    @Operation(summary = "锁定座位")
    public Result<LockSeatResponse> lockSeats(@Valid @RequestBody LockSeatRequest request) {
        LockSeatResponse response = seckillService.lockSeats(request);
        return response.getSuccess() ? Result.success(response) : Result.fail(response.getCode(), response.getMessage());
    }

    @PostMapping("/confirm")
    @Operation(summary = "确认购买")
    public Result<Boolean> confirmPurchase(@Valid @RequestBody ConfirmPurchaseRequest request) {
        Boolean success = seckillService.confirmPurchase(request);
        return success ? Result.success(true) : Result.fail("确认购买失败");
    }

    @PostMapping("/release")
    @Operation(summary = "释放座位")
    public Result<Integer> releaseSeats(@RequestParam Long sessionId,
                                          @RequestParam Long userId,
                                          @RequestParam List<String> seatIds) {
        return Result.success(seckillService.releaseSeats(sessionId, userId, seatIds));
    }

    @GetMapping("/ping")
    @Operation(summary = "健康检查")
    public Result<String> ping() {
        return Result.success("seckill-service is running");
    }
}
