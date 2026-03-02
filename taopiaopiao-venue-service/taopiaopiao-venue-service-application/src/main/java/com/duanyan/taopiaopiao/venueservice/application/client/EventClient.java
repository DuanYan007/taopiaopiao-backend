package com.duanyan.taopiaopiao.venueservice.application.client;

import com.duanyan.taopiaopiao.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 演出服务客户端
 */
@FeignClient(name = "event-service", path = "/admin/events")
public interface EventClient {

    /**
     * 检查场馆是否有非已售完状态的演出
     *
     * @param venueId 场馆ID
     * @return true-有绑定演出，false-无绑定演出
     */
    @GetMapping("/internal/has-active-events/{venueId}")
    Result<Boolean> hasActiveEvents(@PathVariable("venueId") Long venueId);
}
