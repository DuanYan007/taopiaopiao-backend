package com.duanyan.taopiaopiao.seatternplateservice.application.client;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.VenueInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 场馆服务Feign客户端
 *
 * 注意：这里使用本服务定义的 VenueInfo DTO，
 * 而不是直接依赖 venue-service 的 API 模块，
 * 避免微服务之间的耦合
 *
 * @author duanyan
 * @since 1.0.0
 */
@FeignClient(name = "venue-service", path = "/admin/venues")
public interface VenueClient {

    /**
     * 根据ID查询场馆
     *
     * 注意：venue-service 返回的是 Result<VenueResponse>
     * 我们需要确保 VenueInfo 的字段与 VenueResponse 兼容
     * 或者在本服务中做字段映射
     */
    @GetMapping("/{id}")
    Result<VenueInfo> getVenueById(@PathVariable("id") Long id);
}
