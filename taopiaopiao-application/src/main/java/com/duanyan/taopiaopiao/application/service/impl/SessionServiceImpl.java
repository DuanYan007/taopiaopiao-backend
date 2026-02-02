package com.duanyan.taopiaopiao.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.common.exception.BusinessException;
import com.duanyan.taopiaopiao.domain.dto.*;
import com.duanyan.taopiaopiao.domain.entity.*;
import com.duanyan.taopiaopiao.domain.repository.*;
import com.duanyan.taopiaopiao.application.service.SessionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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

    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final TicketTierRepository ticketTierRepository;
    private final ObjectMapper objectMapper;

    @Override
    public SessionPageResponse getSessionPage(SessionQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Session> queryWrapper = new LambdaQueryWrapper<>();

        // 关键词搜索（演出名称、场次名称）
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(Session::getSessionName, request.getKeyword())
                    // 支持通过event_id关联查询演出名称
                    .or()
                    .eq(Session::getEventId, request.getKeyword()));
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
        IPage<Session> sessionPage = sessionRepository.page(page, queryWrapper);

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
        Session session = sessionRepository.findById(id);
        if (session == null) {
            throw new BusinessException(404, "场次不存在");
        }
        return convertToResponse(session);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSession(SessionCreateRequest request) {
        // 验证演出是否存在
        Event event = eventRepository.findById(request.getEventId());
        if (event == null) {
            throw new BusinessException(404, "演出不存在");
        }

        // 验证场馆是否存在
        Venue venue = venueRepository.findById(request.getVenueId());
        if (venue == null) {
            throw new BusinessException(404, "场馆不存在");
        }

        // 验证票档配置
        validateTicketTierConfig(request.getEventId(), request.getTicketTierConfig());

        // 计算总座位数和可售座位数
        int calculatedTotalSeats = request.getTicketTierConfig().stream()
                .filter(tier -> tier.getEnabled() != null && tier.getEnabled())
                .mapToInt(SessionCreateRequest.TicketTierConfigRequest::getSeatCount)
                .sum();

        int calculatedAvailableSeats = request.getTicketTierConfig().stream()
                .filter(tier -> tier.getEnabled() != null && tier.getEnabled())
                .mapToInt(SessionCreateRequest.TicketTierConfigRequest::getAvailableSeats)
                .sum();

        // 验证座位数
        if (request.getTotalSeats() != calculatedTotalSeats) {
            throw new BusinessException(400, "总座位数与票档配置不匹配");
        }

        if (request.getAvailableSeats() > request.getTotalSeats()) {
            throw new BusinessException(400, "可售座位数不能大于总座位数");
        }

        // 转换为实体
        Session session = new Session();
        BeanUtils.copyProperties(request, session, "ticketTierConfig", "metadata");

        // 处理票档配置（转为JSON字符串）
        try {
            session.setTicketTierConfig(objectMapper.writeValueAsString(request.getTicketTierConfig()));
        } catch (Exception e) {
            log.error("票档配置转换失败", e);
            throw new BusinessException(500, "票档配置数据格式错误");
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
        sessionRepository.save(session);

        return session.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSession(Long id, SessionUpdateRequest request) {
        // 检查场次是否存在
        Session existingSession = sessionRepository.findById(id);
        if (existingSession == null) {
            throw new BusinessException(404, "场次不存在");
        }

        // 验证演出是否存在
        Event event = eventRepository.findById(request.getEventId());
        if (event == null) {
            throw new BusinessException(404, "演出不存在");
        }

        // 验证场馆是否存在
        Venue venue = venueRepository.findById(request.getVenueId());
        if (venue == null) {
            throw new BusinessException(404, "场馆不存在");
        }

        // 如果有已售座位，不允许修改关键数据
        if (existingSession.getSoldSeats() != null && existingSession.getSoldSeats() > 0) {
            // 检查是否修改了总座位数
            if (!existingSession.getTotalSeats().equals(request.getTotalSeats())) {
                throw new BusinessException(400, "该场次已有订单，不允许修改总座位数");
            }

            // 检查是否修改了票档座位数
            try {
                List<SessionCreateRequest.TicketTierConfigRequest> newConfig = request.getTicketTierConfig();
                List<SessionTierConfig> oldConfig = objectMapper.readValue(
                        existingSession.getTicketTierConfig(),
                        new TypeReference<List<SessionTierConfig>>() {}
                );

                for (SessionTierConfig oldTier : oldConfig) {
                    for (SessionCreateRequest.TicketTierConfigRequest newTier : newConfig) {
                        if (oldTier.getTierId().equals(newTier.getTierId())) {
                            if (!oldTier.getSeatCount().equals(newTier.getSeatCount())) {
                                throw new BusinessException(400, "该场次已有订单，不允许修改票档座位数");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("票档配置对比失败", e);
            }
        }

        // 验证票档配置
        validateTicketTierConfig(request.getEventId(), request.getTicketTierConfig());

        // 计算总座位数和可售座位数
        int calculatedTotalSeats = request.getTicketTierConfig().stream()
                .filter(tier -> tier.getEnabled() != null && tier.getEnabled())
                .mapToInt(SessionCreateRequest.TicketTierConfigRequest::getSeatCount)
                .sum();

        // 验证座位数
        if (request.getTotalSeats() != calculatedTotalSeats) {
            throw new BusinessException(400, "总座位数与票档配置不匹配");
        }

        if (request.getAvailableSeats() > request.getTotalSeats()) {
            throw new BusinessException(400, "可售座位数不能大于总座位数");
        }

        // 更新字段
        BeanUtils.copyProperties(request, existingSession, "ticketTierConfig", "metadata");

        // 处理票档配置
        try {
            existingSession.setTicketTierConfig(objectMapper.writeValueAsString(request.getTicketTierConfig()));
        } catch (Exception e) {
            log.error("票档配置转换失败", e);
            throw new BusinessException(500, "票档配置数据格式错误");
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
        sessionRepository.update(existingSession);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSession(Long id) {
        // 检查场次是否存在
        Session session = sessionRepository.findById(id);
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
        sessionRepository.deleteById(id);
    }

    @Override
    public void updateSessionStatus(Long id, String status) {
        // 检查场次是否存在
        Session session = sessionRepository.findById(id);
        if (session == null) {
            throw new BusinessException(404, "场次不存在");
        }

        // 验证状态流转
        if (!isValidStatusTransition(session.getStatus(), status)) {
            throw new BusinessException(400, "无效的状态流转: " + session.getStatus() + " -> " + status);
        }

        // 更新状态
        session.setStatus(status);
        sessionRepository.update(session);
    }

    /**
     * 验证票档配置
     */
    private void validateTicketTierConfig(Long eventId, List<SessionCreateRequest.TicketTierConfigRequest> ticketTierConfig) {
        // 查询演出的所有票档
        List<TicketTier> eventTicketTiers = ticketTierRepository.findByEventId(eventId);

        // 验证每个票档配置
        for (SessionCreateRequest.TicketTierConfigRequest config : ticketTierConfig) {
            boolean exists = eventTicketTiers.stream()
                    .anyMatch(tier -> tier.getId().equals(config.getTierId()));

            if (!exists) {
                throw new BusinessException(400, "票档ID " + config.getTierId() + " 不属于该演出");
            }
        }
    }

    /**
     * 验证状态流转是否合法
     */
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        // 允许的状态流转
        // not_started -> on_sale -> sold_out -> ended
        //                 -> off_sale
        if ("not_started".equals(currentStatus) && "on_sale".equals(newStatus)) {
            return true;
        }
        if ("on_sale".equals(currentStatus) && "off_sale".equals(newStatus)) {
            return true;
        }
        if ("off_sale".equals(currentStatus) && "on_sale".equals(newStatus)) {
            return true;
        }
        if ("not_started".equals(currentStatus) && "off_sale".equals(newStatus)) {
            return true;
        }
        // 系统自动状态变更（管理员手动不允许）
        // if ("on_sale".equals(currentStatus) && "sold_out".equals(newStatus)) return true;
        // if ("on_sale".equals(currentStatus) && "ended".equals(newStatus)) return true;

        return false;
    }

    /**
     * 转换为响应DTO
     */
    private SessionResponse convertToResponse(Session session) {
        SessionResponse response = new SessionResponse();
        BeanUtils.copyProperties(session, response);

        // 查询演出信息
        if (session.getEventId() != null) {
            Event event = eventRepository.findById(session.getEventId());
            if (event != null) {
                response.setEventName(event.getName());
            }
        }

        // 查询场馆信息
        if (session.getVenueId() != null) {
            Venue venue = venueRepository.findById(session.getVenueId());
            if (venue != null) {
                response.setVenueName(venue.getName());
            }
        }

        // 生成票档信息列表 - 从演出的票档信息结合场次的配置生成
        response.setTicketTiers(generateTicketTierInfo(session));

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

        return response;
    }

    /**
     * 生成票档信息 - 从演出的票档基础信息结合场次的配置生成
     */
    private List<SessionResponse.TicketTierInfo> generateTicketTierInfo(Session session) {
        if (session.getEventId() == null) {
            return new ArrayList<>();
        }

        // 查询演出的所有票档
        List<TicketTier> eventTicketTiers = ticketTierRepository.findByEventId(session.getEventId());

        // 如果场次有自定义配置，解析配置
        Map<Long, SessionTierConfig> sessionConfigMap = new java.util.HashMap<>();
        if (StringUtils.hasText(session.getTicketTierConfig())) {
            try {
                List<SessionTierConfig> configs = objectMapper.readValue(
                        session.getTicketTierConfig(),
                        new TypeReference<List<SessionTierConfig>>() {}
                );
                sessionConfigMap = configs.stream()
                        .collect(Collectors.toMap(SessionTierConfig::getTierId, config -> config));
            } catch (Exception e) {
                log.error("票档配置解析失败: {}, 将使用演出的默认配置", e.getMessage());
            }
        }

        // 合并演出票档信息和场次配置
        List<SessionResponse.TicketTierInfo> result = new ArrayList<>();
        for (TicketTier tier : eventTicketTiers) {
            SessionTierConfig config = sessionConfigMap.get(tier.getId());

            SessionResponse.TicketTierInfo info = SessionResponse.TicketTierInfo.builder()
                    .id(tier.getId())
                    .name(tier.getName())
                    .price(tier.getPrice().intValue())
                    .color(tier.getColor())
                    .seatCount(config != null ? config.getSeatCount() : 0)
                    .availableSeats(config != null ? config.getAvailableSeats() : 0)
                    .maxPurchase(tier.getMaxPurchase())
                    .enabled(config != null ? config.getEnabled() : true)
                    .build();

            result.add(info);
        }

        return result;
    }

    /**
     * 场次票档配置（用于内部类型转换）
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    private static class SessionTierConfig {
        private Long tierId;
        private Integer seatCount;
        private Integer availableSeats;
        private Boolean enabled;
    }
}
