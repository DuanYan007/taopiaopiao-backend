package com.duanyan.taopiaopiao.sessionservice.application.client.dto;

import lombok.Data;

/**
 * 座位模板DTO（简化版，仅包含场次服务需要的字段）
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
public class SeatTemplateDTO {
    /**
     * 模板ID
     */
    private Long id;

    /**
     * 总座位数
     */
    private Integer totalSeats;

    /**
     * 座位布局数据(JSON字符串)
     */
    private String layoutData;
}
