package com.duanyan.taopiaopiao.orderservice.application.client;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.orderservice.application.client.dto.ConfirmPurchaseRequest;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatRequest;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 秒杀服务客户端
 */
@FeignClient(name = "seckill-service", path = "/seckill")
public interface SeckillClient {

    @PostMapping("/lock")
    Result<LockSeatResponse> lockSeats(@RequestBody LockSeatRequest request);

    @PostMapping("/confirm")
    Result<Boolean> confirmPurchase(@RequestBody ConfirmPurchaseRequest request);

    @PostMapping("/release")
    Result<Integer> releaseSeats(@RequestParam("sessionId") Long sessionId,
                                @RequestParam("userId") Long userId,
                                @RequestParam("seatIds") List<String> seatIds);
}
