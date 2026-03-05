package com.duanyan.taopiaopiao.orderservice.application.service.impl;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.orderservice.api.dto.CreateOrderRequest;
import com.duanyan.taopiaopiao.orderservice.api.dto.OrderResponse;
import com.duanyan.taopiaopiao.orderservice.api.dto.SeatDetail;
import com.duanyan.taopiaopiao.orderservice.application.client.SeckillClient;
import com.duanyan.taopiaopiao.orderservice.application.client.dto.ConfirmPurchaseRequest;
import com.duanyan.taopiaopiao.orderservice.application.client.SessionClient;
import com.duanyan.taopiaopiao.orderservice.application.client.dto.SessionDTO;
import com.duanyan.taopiaopiao.orderservice.application.mapper.OrderMapper;
import com.duanyan.taopiaopiao.orderservice.application.service.OrderService;
import com.duanyan.taopiaopiao.orderservice.domain.entity.Order;
import com.duanyan.taopiaopiao.orderservice.domain.enums.OrderStatus;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final SeckillClient seckillClient;
    private final SessionClient sessionClient;

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        // 1. 获取场次信息
        Result<SessionDTO> sessionResult = sessionClient.getSessionById(request.getSessionId());
        if (sessionResult == null || sessionResult.getData() == null) {
            throw new RuntimeException("场次不存在");
        }
        SessionDTO session = sessionResult.getData();

        // 2. 确定eventId
        Long eventId = request.getEventId();
        if (eventId == null) {
            eventId = session.getEventId();
            if (eventId == null) {
                throw new RuntimeException("场次关联的演出信息不存在");
            }
        }

        // 3. 校验请求数据
        if (request.getSeatIds().size() != request.getSeatDetails().size()) {
            throw new RuntimeException("座位ID列表与座位详情数量不一致");
        }

        // 4. 校验总金额
        BigDecimal calculatedTotal = request.getSeatDetails().stream()
                .map(SeatDetail::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (calculatedTotal.compareTo(request.getTotalAmount()) != 0) {
            throw new RuntimeException("订单总金额与座位详情金额不一致");
        }

        if (request.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("订单金额必须大于0");
        }

        // 5. 确认购买（前端已预先锁定座位）
        ConfirmPurchaseRequest confirmRequest = new ConfirmPurchaseRequest();
        confirmRequest.setSessionId(request.getSessionId());
        confirmRequest.setUserId(userId);
        confirmRequest.setSeatIds(request.getSeatIds());

        Result<Boolean> confirmResult = seckillClient.confirmPurchase(confirmRequest);
        if (confirmResult == null || !confirmResult.isSuccess() || confirmResult.getData() == null) {
            String message = (confirmResult != null && confirmResult.getMsg() != null) ? confirmResult.getMsg() : "确认购买失败";
            throw new RuntimeException("确认购买失败: " + message);
        }

        // 6. 创建订单（状态直接为已支付）
        String orderNo = generateOrderNo();
        BigDecimal totalAmount = request.getTotalAmount();
        LocalDateTime now = LocalDateTime.now();

        // 计算平均单价（用于记录）
        BigDecimal unitPrice = totalAmount.divide(
                BigDecimal.valueOf(request.getSeatIds().size()),
                2,
                BigDecimal.ROUND_HALF_UP);

        Order order = Order.builder()
                .orderNo(orderNo)
                .userId(userId)
                .sessionId(request.getSessionId())
                .eventId(eventId)
                .seatIds(String.join(",", request.getSeatIds()))
                .seatCount(request.getSeatIds().size())
                .unitPrice(unitPrice)
                .totalAmount(totalAmount)
                .status(OrderStatus.PAID.getCode())
                .payTime(now)
                .expireTime(now)  // 已支付订单的过期时间等于支付时间
                .build();

        orderMapper.insert(order);

        log.info("支付成功，创建订单: orderNo={}, userId={}, amount={}, seats={}",
                orderNo, userId, totalAmount, request.getSeatIds());

        return buildOrderResponse(order);
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
        Result<Integer> result = seckillClient.releaseSeats(order.getSessionId(), userId, seatIds);

        if (result == null || !result.isSuccess()) {
            log.warn("释放座位失败: orderNo={}", orderNo);
        }

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
                Result<Integer> result = seckillClient.releaseSeats(order.getSessionId(), order.getUserId(), seatIds);

                if (result != null && result.isSuccess()) {
                    order.setStatus(OrderStatus.TIMEOUT.getCode());
                    orderMapper.updateById(order);
                    log.info("订单超时取消: orderNo={}", order.getOrderNo());
                }
            } catch (Exception e) {
                log.error("取消超时订单失败: orderNo={}", order.getOrderNo(), e);
            }
        }
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
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
