package com.duanyan.taopiaopiao.orderservice.application.service;

import com.duanyan.taopiaopiao.orderservice.api.dto.CreateOrderRequest;
import com.duanyan.taopiaopiao.orderservice.api.dto.OrderResponse;
import com.duanyan.taopiaopiao.orderservice.api.dto.PayOrderRequest;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     */
    OrderResponse createOrder(Long userId, CreateOrderRequest request);

    /**
     * 支付订单
     */
    Boolean payOrder(Long userId, PayOrderRequest request);

    /**
     * 取消订单
     */
    Boolean cancelOrder(Long userId, String orderNo);

    /**
     * 查询订单
     */
    OrderResponse getOrderByNo(Long userId, String orderNo);

    /**
     * 订单超时取消（定时任务调用）
     */
    void cancelTimeoutOrders();
}
