package com.duanyan.taopiaopiao.orderservice.application.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 座位模板信息DTO（order-service内部使用）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatTemplateDTO {

    /**
     * 模板ID
     */
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 关联场馆ID
     */
    private Long venueId;
}
