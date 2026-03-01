package com.duanyan.taopiaopiao.orderservice.application.client;

import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatRequest;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 秒杀服务客户端
 */
@FeignClient(name = "seckill-service", path = "/api/seckill")
public interface SeckillClient {

    @PostMapping("/lock")
    LockSeatResponse lockSeats(@RequestBody LockSeatRequest request);

    @PostMapping("/confirm")
    Boolean confirmPurchase(@RequestBody LockSeatRequest request);

    @PostMapping("/release")
    Integer releaseSeats(Long sessionId, Long userId, java.util.List<String> seatIds);
}
