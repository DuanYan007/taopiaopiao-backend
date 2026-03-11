package com.duanyan.taopiaopiao.sessionservice.application.client;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.sessionservice.application.client.dto.EventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 演出服务Feign Client
 *
 * @author duanyan
 * @since 1.0.0
 */
@FeignClient(name = "event-service", path = "/admin/events")
public interface EventClient {

    /**
     * 根据ID查询演出
     *
     * @param id 演出ID
     * @return 演出数据
     */
    @GetMapping("/{id}")
    Result<EventDTO> getEventById(@PathVariable("id") Long id);
}