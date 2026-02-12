package com.duanyan.taopiaopiao.sessionservice.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.common.exception.BusinessException;
import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventResponse;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionCreateRequest;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionPageResponse;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionQueryRequest;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionResponse;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionUpdateRequest;
import com.duanyan.taopiaopiao.sessionservice.application.client.EventClient;
import com.duanyan.taopiaopiao.sessionservice.application.client.VenueClient;
import com.duanyan.taopiaopiao.sessionservice.application.mapper.SessionMapper;
import com.duanyan.taopiaopiao.sessionservice.application.service.SessionService;
import com.duanyan.taopiaopiao.sessionservice.domain.entity.Session;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final VenueClient venueClient;
    private final EventClient eventClient;

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

        // 收集需要查询的 venueId 和 eventId
        List<Long> venueIds = sessionPage.getRecords().stream()
                .map(Session::getVenueId)
                .filter(id -> id != null)
                .distinct()
                .toList();
        List<Long> eventIds = sessionPage.getRecords().stream()
                .map(Session::getEventId)
                .filter(id -> id != null)
                .distinct()
                .toList();

        // 批量查询关联信息
        Map<Long, VenueResponse> venueMap = fetchVenuesByIds(venueIds);
        Map<Long, EventResponse> eventMap = fetchEventsByIds(eventIds);

        // 转换为DTO并填充关联信息
        List<SessionResponse> sessionResponseList = sessionPage.getRecords().stream()
                .map(session -> convertToResponse(session, venueMap, eventMap))
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

        // 查询关联的场馆和演出信息
        Map<Long, VenueResponse> venueMap = fetchVenuesByIds(List.of(session.getVenueId()));
        Map<Long, EventResponse> eventMap = fetchEventsByIds(List.of(session.getEventId()));

        return convertToResponse(session, venueMap, eventMap);
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

        // 清空 updatedAt，让 MyBatis-Plus 自动填充
        existingSession.setUpdatedAt(null);

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
        // 清空 updatedAt，让 MyBatis-Plus 自动填充
        session.setUpdatedAt(null);
        sessionMapper.updateById(session);
    }

    /**
     * 转换为响应DTO（带关联信息）
     */
    private SessionResponse convertToResponse(Session session,
                                               Map<Long, VenueResponse> venueMap,
                                               Map<Long, EventResponse> eventMap) {
        SessionResponse response = new SessionResponse();
        BeanUtils.copyProperties(session, response);

        // 填充演出名称
        if (session.getEventId() != null && eventMap != null) {
            EventResponse event = eventMap.get(session.getEventId());
            if (event != null) {
                response.setEventName(event.getName());
            }
        }

        // 填充场馆名称
        if (session.getVenueId() != null && venueMap != null) {
            VenueResponse venue = venueMap.get(session.getVenueId());
            if (venue != null) {
                response.setVenueName(venue.getName());
            }
        }

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
     * 转换为响应DTO（无关联信息，兼容其他调用）
     */
    private SessionResponse convertToResponse(Session session) {
        return convertToResponse(session, null, null);
    }

    /**
     * 批量获取场馆信息
     */
    private Map<Long, VenueResponse> fetchVenuesByIds(List<Long> venueIds) {
        if (venueIds == null || venueIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, VenueResponse> result = new HashMap<>();
        for (Long id : venueIds) {
            try {
                // 调用 Feign Client，返回 Result<VenueResponse>
                Result<VenueResponse> resp = venueClient.getVenueById(id);
                // 由于 V 泛型固定，直接使用 resp.getData()
                VenueResponse venueResp = resp.getData();
                if (venueResp != null) {
                    result.put(id, venueResp);
                }
            } catch (Exception e) {
                log.error("查询场馆信息失败, venueId: {}, error: {}", id, e.getMessage());
            }
        }
        return result;
    }

    /**
     * 批量获取演出信息
     */
    private Map<Long, EventResponse> fetchEventsByIds(List<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, EventResponse> result = new HashMap<>();
        for (Long id : eventIds) {
            try {
                // 调用 Feign Client，返回 Result<EventResponse>
                Result<EventResponse> resp = eventClient.getEventById(id);
                // 由于 V 泛型固定，直接使用 resp.getData()
                EventResponse eventResp = resp.getData();
                if (eventResp != null) {
                    result.put(id, eventResp);
                }
            } catch (Exception e) {
                log.error("查询演出信息失败, eventId: {}, error: {}", id, e.getMessage());
            }
        }
        return result;
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
