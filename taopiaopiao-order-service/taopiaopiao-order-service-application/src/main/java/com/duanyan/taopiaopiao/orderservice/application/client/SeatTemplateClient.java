package com.duanyan.taopiaopiao.orderservice.application.client;

import com.duanyan.taopiaopiao.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 座位模板服务客户端
 */
@FeignClient(name = "seat-template-service")
public interface SeatTemplateClient {

    /**
     * 获取座位模板的最低价格
     *
     * @param templateId 模板ID
     * @return 最低价格
     */
    @GetMapping("/client/seat-templates/{templateId}/min-price")
    Result<java.math.BigDecimal> getMinPrice(@PathVariable("templateId") Long templateId);
}
