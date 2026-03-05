package com.duanyan.taopiaopiao.orderservice.application.client;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.orderservice.application.client.dto.SessionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 场次服务客户端
 *
 * @author duanyan
 * @since 1.0.0
 */
@FeignClient(name = "session-service", path = "/client/sessions")
public interface SessionClient {

    /**
     * 根据ID查询场次详情
     *
     * @param id 场次ID
     * @return 场次信息
     */
    @GetMapping("/{id}")
    Result<SessionDTO> getSessionById(@PathVariable("id") Long id);
}
