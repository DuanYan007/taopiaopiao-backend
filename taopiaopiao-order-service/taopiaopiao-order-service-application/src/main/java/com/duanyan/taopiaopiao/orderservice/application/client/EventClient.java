package com.duanyan.taopiaopiao.orderservice.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 演出服务客户端
 */
@FeignClient(name = "event-service")
public interface EventClient {

    @GetMapping("/admin/events/price")
    BigDecimal getEventPrice(@RequestParam("eventId") Long eventId);
}
