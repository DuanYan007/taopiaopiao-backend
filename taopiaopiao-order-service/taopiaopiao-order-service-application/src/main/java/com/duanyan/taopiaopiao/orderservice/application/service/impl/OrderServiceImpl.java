package com.duanyan.taopiaopiao.orderservice.application.service.impl;

import com.duanyan.taopiaopiao.orderservice.api.dto.CreateOrderRequest;
import com.duanyan.taopiaopiao.orderservice.api.dto.OrderResponse;
import com.duanyan.taopiaopiao.orderservice.api.dto.PayOrderRequest;
import com.duanyan.taopiaopiao.orderservice.application.client.EventClient;
import com.duanyan.taopiaopiao.orderservice.application.client.SeckillClient;
import com.duanyan.taopiaopiao.orderservice.application.mapper.OrderMapper;
import com.duanyan.taopiaopiao.orderservice.application.service.OrderService;
import com.duanyan.taopiaopiao.orderservice.domain.entity.Order;
import com.duanyan.taopiaopiao.orderservice.domain.enums.OrderStatus;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatRequest;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final SeckillClient seckillClient;
    private final EventClient eventClient;

    private static final int ORDER_EXPIRE_MINUTES = 15;

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        // 1. 获取演出价格
        BigDecimal unitPrice = eventClient.getEventPrice(request.getEventId());
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("演出价格无效");
        }

        // 2. 锁定座位
        LockSeatRequest lockRequest = new LockSeatRequest();
        lockRequest.setSessionId(request.getSessionId());
        lockRequest.setUserId(userId);
        lockRequest.setSeatIds(request.getSeatIds());
        lockRequest.setExpireSeconds(ORDER_EXPIRE_MINUTES * 60);

        LockSeatResponse lockResponse = seckillClient.lockSeats(lockRequest);
        if (!lockResponse.getSuccess()) {
            throw new RuntimeException("锁座失败: " + lockResponse.getMessage());
        }

        // 3. 创建订单
        String orderNo = generateOrderNo();
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(request.getSeatIds().size()));

        Order order = Order.builder()
                .orderNo(orderNo)
                .userId(userId)
                .sessionId(request.getSessionId())
                .eventId(request.getEventId())
                .seatIds(String.join(",", request.getSeatIds()))
                .seatCount(request.getSeatIds().size())
                .unitPrice(unitPrice)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING.getCode())
                .expireTime(LocalDateTime.now().plusMinutes(ORDER_EXPIRE_MINUTES))
                .build();

        orderMapper.insert(order);

        log.info("创建订单成功: orderNo={}, userId={}, amount={}", orderNo, userId, totalAmount);

        return buildOrderResponse(order);
    }

    @Override
    @Transactional
    public Boolean payOrder(Long userId, PayOrderRequest request) {
        Order order = orderMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNo, request.getOrderNo())
                        .eq(Order::getUserId, userId)
        );

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        if (LocalDateTime.now().isAfter(order.getExpireTime())) {
            order.setStatus(OrderStatus.TIMEOUT.getCode());
            orderMapper.updateById(order);
            throw new RuntimeException("订单已超时");
        }

        // 确认购买座位
        List<String> seatIds = List.of(order.getSeatIds().split(","));
        LockSeatRequest confirmRequest = new LockSeatRequest();
        confirmRequest.setSessionId(order.getSessionId());
        confirmRequest.setUserId(userId);
        confirmRequest.setSeatIds(seatIds);

        Boolean success = seckillClient.confirmPurchase(confirmRequest);
        if (!success) {
            throw new RuntimeException("确认购买失败");
        }

        // 更新订单状态
        order.setStatus(OrderStatus.PAID.getCode());
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);

        log.info("订单支付成功: orderNo={}, userId={}", request.getOrderNo(), userId);
        return true;
    }

    @Override
    @Transactional
    public Boolean cancelOrder(Long userId, String orderNo) {
        Order order = orderMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNo, orderNo)
                        .eq(Order::getUserId, userId)
        );

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
            throw new RuntimeException("只能取消待支付订单");
        }

        // 释放座位
        List<String> seatIds = List.of(order.getSeatIds().split(","));
        seckillClient.releaseSeats(order.getSessionId(), userId, seatIds);

        // 更新订单状态
        order.setStatus(OrderStatus.CANCELLED.getCode());
        orderMapper.updateById(order);

        log.info("订单取消成功: orderNo={}, userId={}", orderNo, userId);
        return true;
    }

    @Override
    public OrderResponse getOrderByNo(Long userId, String orderNo) {
        Order order = orderMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNo, orderNo)
                        .eq(Order::getUserId, userId)
        );

        if (order == null) {
            return null;
        }

        return buildOrderResponse(order);
    }

    @Override
    @Transactional
    public void cancelTimeoutOrders() {
        List<Order> timeoutOrders = orderMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order>()
                        .eq(Order::getStatus, OrderStatus.PENDING.getCode())
                        .lt(Order::getExpireTime, LocalDateTime.now())
        );

        for (Order order : timeoutOrders) {
            try {
                List<String> seatIds = List.of(order.getSeatIds().split(","));
                seckillClient.releaseSeats(order.getSessionId(), order.getUserId(), seatIds);

                order.setStatus(OrderStatus.TIMEOUT.getCode());
                orderMapper.updateById(order);

                log.info("订单超时取消: orderNo={}", order.getOrderNo());
            } catch (Exception e) {
                log.error("取消超时订单失败: orderNo={}", order.getOrderNo(), e);
            }
        }
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private OrderResponse buildOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        BeanUtils.copyProperties(order, response);

        // 设置状态描述
        OrderStatus status = OrderStatus.fromCode(order.getStatus());
        if (status != null) {
            response.setStatusDesc(status.getDesc());
        }

        // 解析座位ID列表
        if (order.getSeatIds() != null) {
            response.setSeatIds(List.of(order.getSeatIds().split(",")));
        }

        return response;
    }
}
