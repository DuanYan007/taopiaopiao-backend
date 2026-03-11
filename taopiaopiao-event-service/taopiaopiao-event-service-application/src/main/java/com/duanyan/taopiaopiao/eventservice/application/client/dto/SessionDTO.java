package com.duanyan.taopiaopiao.eventservice.application.client.dto;

import lombok.Data;

/**
 * 场次信息DTO（event-service内部使用）
 */
@Data
public class SessionDTO {

    /**
     * 场次ID
     */
    private Long id;

    /**
     * 演出ID
     */
    private Long eventId;

    /**
     * 场次名称
     */
    private String sessionName;

    /**
     * 座位模板ID
     */
    private Long seatTemplateId;
}
