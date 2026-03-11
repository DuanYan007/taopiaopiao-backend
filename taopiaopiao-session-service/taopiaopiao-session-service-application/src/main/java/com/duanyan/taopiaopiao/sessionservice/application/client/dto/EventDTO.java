package com.duanyan.taopiaopiao.sessionservice.application.client.dto;

import lombok.Data;

/**
 * 演出信息DTO（简化版，仅包含场次服务需要的字段）
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
public class EventDTO {
    /**
     * 演出ID
     */
    private Long id;

    /**
     * 演出名称
     */
    private String name;
}
