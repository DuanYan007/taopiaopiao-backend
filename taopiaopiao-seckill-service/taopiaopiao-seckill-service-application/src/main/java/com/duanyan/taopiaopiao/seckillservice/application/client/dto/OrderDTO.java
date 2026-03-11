package com.duanyan.taopiaopiao.seckillservice.application.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单DTO（秒杀服务调用订单服务）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 场次ID
     */
    private Long sessionId;

    /**
     * 演出ID
     */
    private Long eventId;

    /**
     * 座位ID列表
     */
    private List<String> seatIds;

    /**
     * 座位数量
     */
    private Integer seatCount;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;
}
