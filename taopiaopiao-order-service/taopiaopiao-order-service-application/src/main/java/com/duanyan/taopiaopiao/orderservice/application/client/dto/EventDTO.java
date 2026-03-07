package com.duanyan.taopiaopiao.orderservice.application.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 演出信息DTO（order-service内部使用）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    /**
     * 演出ID
     */
    private Long id;

    /**
     * 演出名称
     */
    private String name;

    /**
     * 封面图片URL
     */
    private String coverImage;
}
