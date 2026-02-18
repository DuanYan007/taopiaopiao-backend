package com.duanyan.taopiaopiao.sessionservice.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.common.exception.BusinessException;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventResponse;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionPageResponse;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionQueryRequest;
import com.duanyan.taopiaopiao.sessionservice.api.dto.SessionResponse;
import com.duanyan.taopiaopiao.sessionservice.application.client.EventClient;
import com.duanyan.taopiaopiao.sessionservice.application.client.VenueClient;
import com.duanyan.taopiaopiao.sessionservice.application.mapper.SessionMapper;
import com.duanyan.taopiaopiao.sessionservice.application.service.ClientSessionService;
import com.duanyan.taopiaopiao.sessionservice.domain.entity.Session;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 场次客户端服务实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientSessionServiceImpl implements ClientSessionService {

    private final SessionMapper sessionMapper;
    private final VenueClient venueClient;
    private final EventClient eventClient;
    private final SessionServiceImpl sessionService; // 复用管理端的转换逻辑

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

        // 状态筛选 - 客户端只显示有效状态
        if (StringUtils.hasText(request.getStatus())) {
            queryWrapper.eq(Session::getStatus, request.getStatus());
        } else {
            // 默认只显示可售/即将开始的场次，不显示已结束/已取消的
            queryWrapper.notIn(Session::getStatus, "ended", "cancelled");
        }

        // 按开始时间升序排序（最近的场次在前）
        queryWrapper.orderByAsc(Session::getStartTime);

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

        // 检查场次状态（客户端不能查看已取消的场次）
        if ("cancelled".equals(session.getStatus())) {
            throw new BusinessException(404, "场次不存在");
        }

        // 查询关联的场馆和演出信息
        Map<Long, VenueResponse> venueMap = fetchVenuesByIds(List.of(session.getVenueId()));
        Map<Long, EventResponse> eventMap = fetchEventsByIds(List.of(session.getEventId()));

        return convertToResponse(session, venueMap, eventMap);
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
                var resp = venueClient.getVenueById(id);
                if (resp != null && resp.getData() != null) {
                    result.put(id, resp.getData());
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
                var resp = eventClient.getEventById(id);
                if (resp != null && resp.getData() != null) {
                    result.put(id, resp.getData());
                }
            } catch (Exception e) {
                log.error("查询演出信息失败, eventId: {}, error: {}", id, e.getMessage());
            }
        }
        return result;
    }

    /**
     * 转换为响应DTO（带关联信息）
     */
    private SessionResponse convertToResponse(Session session,
                                               Map<Long, VenueResponse> venueMap,
                                               Map<Long, EventResponse> eventMap) {
        // 直接复用管理端服务的查询方法
        return sessionService.getSessionById(session.getId());
    }
}
