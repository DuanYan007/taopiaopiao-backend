package com.duanyan.taopiaopiao.sessionservice.application.client;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.sessionservice.application.client.dto.SeatTemplateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 座位模板服务Feign Client
 *
 * @author duanyan
 * @since 1.0.0
 */
@FeignClient(name = "seat-template-service", path = "/admin/seat-templates")
public interface SeatTemplateClient {

    /**
     * 根据ID查询座位模板
     *
     * @param id 模板ID
     * @return 座位模板数据
     */
    @GetMapping("/{id}")
    Result<SeatTemplateDTO> getTemplateById(@PathVariable("id") Long id);
}
