package com.duanyan.taopiaopiao.sessionservice.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.common.exception.BusinessException;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionCreateRequest;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionPageResponse;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionQueryRequest;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionResponse;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionUpdateRequest;
import com.duanyan.taopiaopiao.sessionservice.application.mapper.SessionMapper;
import com.duanyan.taopiaopiao.sessionservice.application.service.SessionService;
import com.duanyan.taopiaopiao.sessionservice.domain.entity.Session;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 场次服务实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;
    private final ObjectMapper objectMapper;

    @Override
    public SessionPageResponse getSessionPage(SessionQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Session> queryWrapper = new LambdaQueryWrapper<>();

        // 关键词搜索（场次名称）
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.like(Session::getSessionName, request.getKeyword());
        }

        // 演出筛选
        if (request.getEventId() != null) {
            queryWrapper.eq(Session::getEventId, request.getEventId());
        }

        // 场馆筛选
        if (request.getVenueId() != null) {
            queryWrapper.eq(Session::getVenueId, request.getVenueId());
        }

        // 状态筛选
        if (StringUtils.hasText(request.getStatus())) {
            queryWrapper.eq(Session::getStatus, request.getStatus());
        }

        // 按开始时间倒序排序
        queryWrapper.orderByDesc(Session::getStartTime);

        // 分页查询
        Page<Session> page = new Page<>(request.getPage(), request.getPageSize());
        IPage<Session> sessionPage = sessionMapper.selectPage(page, queryWrapper);

        // 转换为DTO
        List<SessionResponse> sessionResponseList = sessionPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        // 计算总页数
        int totalPages = (int) Math.ceil((double) sessionPage.getTotal() / request.getPageSize());

        return SessionPageResponse.builder()
                .list(sessionResponseList)
                .total(sessionPage.getTotal())
                .page(request.getPage())
                .pageSize(request.getPageSize())
                .totalPages(totalPages)
                .build();
    }

    @Override
    public SessionResponse getSessionById(Long id) {
        Session session = sessionMapper.selectById(id);
        if (session == null) {
            throw new BusinessException(404, "场次不存在");
        }
        return convertToResponse(session);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSession(SessionCreateRequest request) {
        // 转换为实体
        Session session = new Session();
        BeanUtils.copyProperties(request, session, "ticketTierConfig", "metadata");

        // 处理票档配置（转为JSON字符串）
        if (request.getTicketTierConfig() != null && !request.getTicketTierConfig().isEmpty()) {
            try {
                session.setTicketTierConfig(objectMapper.writeValueAsString(request.getTicketTierConfig()));
            } catch (Exception e) {
                log.error("票档配置转换失败", e);
                throw new BusinessException(500, "票档配置数据格式错误");
            }
        }

        // 处理metadata（扩展配置）
        if (request.getMetadata() != null) {
            try {
                session.setMetadata(objectMapper.writeValueAsString(request.getMetadata()));
            } catch (Exception e) {
                log.error("metadata转换失败", e);
                throw new BusinessException(500, "扩展数据格式错误");
            }
        }

        // 初始化座位数
        session.setSoldSeats(0);
        session.setLockedSeats(0);

        // 保存场次
        sessionMapper.insert(session);

        return session.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSession(Long id, SessionUpdateRequest request) {
        // 检查场次是否存在
        Session existingSession = sessionMapper.selectById(id);
        if (existingSession == null) {
            throw new BusinessException(404, "场次不存在");
        }

        // 更新字段
        BeanUtils.copyProperties(request, existingSession, "ticketTierConfig", "metadata");

        // 处理票档配置
        if (request.getTicketTierConfig() != null && !request.getTicketTierConfig().isEmpty()) {
            try {
                existingSession.setTicketTierConfig(objectMapper.writeValueAsString(request.getTicketTierConfig()));
            } catch (Exception e) {
                log.error("票档配置转换失败", e);
                throw new BusinessException(500, "票档配置数据格式错误");
            }
        }

        // 处理metadata
        if (request.getMetadata() != null) {
            try {
                existingSession.setMetadata(objectMapper.writeValueAsString(request.getMetadata()));
            } catch (Exception e) {
                log.error("metadata转换失败", e);
                throw new BusinessException(500, "扩展数据格式错误");
            }
        }

        // 保存更新
        sessionMapper.updateById(existingSession);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSession(Long id) {
        // 检查场次是否存在
        Session session = sessionMapper.selectById(id);
        if (session == null) {
            throw new BusinessException(404, "场次不存在");
        }

        // 检查是否可以删除
        if (session.getSoldSeats() != null && session.getSoldSeats() > 0) {
            throw new BusinessException(400, "该场次已有订单，无法删除");
        }

        // 检查状态
        if ("ended".equals(session.getStatus())) {
            throw new BusinessException(400, "已结束的场次不允许删除");
        }

        // 删除
        sessionMapper.deleteById(id);
    }

    @Override
    public void updateSessionStatus(Long id, String status) {
        // 检查场次是否存在
        Session session = sessionMapper.selectById(id);
        if (session == null) {
            throw new BusinessException(404, "场次不存在");
        }

        // 更新状态
        session.setStatus(status);
        sessionMapper.updateById(session);
    }

    /**
     * 转换为响应DTO
     */
    private SessionResponse convertToResponse(Session session) {
        SessionResponse response = new SessionResponse();
        BeanUtils.copyProperties(session, response);

        // 处理metadata
        if (StringUtils.hasText(session.getMetadata())) {
            try {
                response.setMetadata(objectMapper.readValue(
                        session.getMetadata(),
                        SessionResponse.SessionMetadata.class
                ));
            } catch (Exception e) {
                log.error("metadata解析失败: {}", e.getMessage());
            }
        }

        // 生成票档信息列表 - 从票档配置中解析
        response.setTicketTiers(generateTicketTierInfo(session));

        return response;
    }

    /**
     * 生成票档信息 - 从场次的配置生成
     */
    private List<SessionResponse.TicketTierInfo> generateTicketTierInfo(Session session) {
        List<SessionResponse.TicketTierInfo> result = new ArrayList<>();

        if (StringUtils.hasText(session.getTicketTierConfig())) {
            try {
                List<SessionCreateRequest.TicketTierConfigRequest> configs = objectMapper.readValue(
                        session.getTicketTierConfig(),
                        new TypeReference<List<SessionCreateRequest.TicketTierConfigRequest>>() {}
                );

                for (SessionCreateRequest.TicketTierConfigRequest config : configs) {
                    SessionResponse.TicketTierInfo info = SessionResponse.TicketTierInfo.builder()
                            .id(config.getTierId())
                            .price(config.getBasePrice() != null ? config.getBasePrice() :
                                   (config.getOverridePrice() != null ? config.getOverridePrice() : 0))
                            .seatCount(config.getSeatCount())
                            .availableSeats(config.getAvailableSeats())
                            .maxPurchase(config.getMaxPurchase())
                            .enabled(config.getEnabled() != null ? config.getEnabled() : true)
                            .build();

                    result.add(info);
                }
            } catch (Exception e) {
                log.error("票档配置解析失败: {}", e.getMessage());
            }
        }

        return result;
    }
}
