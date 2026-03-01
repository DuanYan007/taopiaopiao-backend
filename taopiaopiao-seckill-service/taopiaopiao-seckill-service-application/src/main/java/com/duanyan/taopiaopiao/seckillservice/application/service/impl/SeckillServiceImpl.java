package com.duanyan.taopiaopiao.seckillservice.application.service.impl;

import com.duanyan.taopiaopiao.common.redis.service.RedisService;
import com.duanyan.taopiaopiao.seckillservice.api.dto.ConfirmPurchaseRequest;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatRequest;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatResponse;
import com.duanyan.taopiaopiao.seckillservice.application.mapper.SeatLockMapper;
import com.duanyan.taopiaopiao.seckillservice.application.service.SeckillService;
import com.duanyan.taopiaopiao.seckillservice.domain.entity.SeatLock;
import com.duanyan.taopiaopiao.seckillservice.domain.enums.LockStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeckillServiceImpl implements SeckillService {

    private final RedisService redisService;
    private final SeatLockMapper seatLockMapper;

    @Override
    @Transactional
    public LockSeatResponse lockSeats(LockSeatRequest request) {
        Long sessionId = request.getSessionId();
        Long userId = request.getUserId();
        List<String> seatIds = request.getSeatIds();
        Integer expireSeconds = request.getExpireSeconds() != null ? request.getExpireSeconds() : 300;

        String lockId = UUID.randomUUID().toString().replace("-", "");
        long expireTime = System.currentTimeMillis() + expireSeconds * 1000L;

        int code = redisService.lockSeats(sessionId, userId, seatIds, expireSeconds);

        if (code == 0) {
            for (String seatId : seatIds) {
                String[] parts = seatId.split(":");
                SeatLock seatLock = SeatLock.builder()
                        .sessionId(sessionId)
                        .userId(userId)
                        .seatId(seatId)
                        .seatRow(Integer.parseInt(parts[0]))
                        .seatCol(Integer.parseInt(parts[1]))
                        .lockTime(System.currentTimeMillis())
                        .expireTime(expireTime)
                        .status(LockStatus.LOCKED.getCode())
                        .orderId("ORDER_" + lockId)
                        .build();
                seatLockMapper.insert(seatLock);
            }
            log.info("锁座成功: sessionId={}, userId={}, lockId={}", sessionId, userId, lockId);
            return LockSeatResponse.builder()
                    .success(true)
                    .code(0)
                    .message("锁座成功")
                    .lockedSeats(seatIds)
                    .lockId(lockId)
                    .build();
        }

        String message = switch (code) {
            case 1 -> "座位不存在";
            case 2 -> "座位已被锁定或售出";
            case 3 -> "您已锁定或购买了该座位";
            default -> "系统错误";
        };

        log.warn("锁座失败: code={}, message={}", code, message);
        return LockSeatResponse.builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    @Transactional
    public Boolean confirmPurchase(ConfirmPurchaseRequest request) {
        boolean success = redisService.confirmPurchase(
                request.getSessionId(), request.getUserId(), request.getSeatIds());

        if (success) {
            for (String seatId : request.getSeatIds()) {
                seatLockMapper.updateStatus(
                        request.getSessionId(), request.getUserId(), seatId, LockStatus.PURCHASED.getCode());
            }
            log.info("确认购买成功: sessionId={}, userId={}",
                    request.getSessionId(), request.getUserId());
        }
        return success;
    }

    @Override
    @Transactional
    public Integer releaseSeats(Long sessionId, Long userId, List<String> seatIds) {
        int count = redisService.unlockSeats(sessionId, userId, seatIds);
        if (count > 0) {
            for (String seatId : seatIds) {
                seatLockMapper.updateStatus(sessionId, userId, seatId, LockStatus.RELEASED.getCode());
            }
            log.info("释放座位成功: sessionId={}, userId={}, count={}", sessionId, userId, count);
        }
        return count;
    }
}
