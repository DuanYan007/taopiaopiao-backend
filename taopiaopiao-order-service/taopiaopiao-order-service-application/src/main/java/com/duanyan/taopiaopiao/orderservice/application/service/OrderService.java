package com.duanyan.taopiaopiao.orderservice.application.service;

import com.duanyan.taopiaopiao.orderservice.api.dto.CreateOrderRequest;
import com.duanyan.taopiaopiao.orderservice.api.dto.OrderPageRequest;
import com.duanyan.taopiaopiao.orderservice.api.dto.OrderPageResponse;
import com.duanyan.taopiaopiao.orderservice.api.dto.OrderResponse;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单（支付并创建）
     */
    OrderResponse createOrder(Long userId, CreateOrderRequest request);

    /**
     * 订单分页查询
     */
    OrderPageResponse getOrderPage(Long userId, OrderPageRequest request);

    /**
     * 查询订单
     */
    OrderResponse getOrderByNo(Long userId, String orderNo);

    /**
     * 取消订单
     */
    Boolean cancelOrder(Long userId, String orderNo);

    /**
     * 删除订单
     */
    Boolean deleteOrder(Long userId, String orderNo);

    /**
     * 订单超时取消（定时任务调用）
     */
    void cancelTimeoutOrders();
}
