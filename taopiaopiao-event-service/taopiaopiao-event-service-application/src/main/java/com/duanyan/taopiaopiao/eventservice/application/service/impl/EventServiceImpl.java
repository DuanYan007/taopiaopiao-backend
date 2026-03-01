package com.duanyan.taopiaopiao.eventservice.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.common.exception.BusinessException;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventCreateRequest;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventPageResponse;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventQueryRequest;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventResponse;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventUpdateRequest;
import com.duanyan.taopiaopiao.eventservice.application.mapper.EventMapper;
import com.duanyan.taopiaopiao.eventservice.application.mapper.TicketTierMapper;
import com.duanyan.taopiaopiao.eventservice.application.service.EventService;
import com.duanyan.taopiaopiao.eventservice.domain.entity.Event;
import com.duanyan.taopiaopiao.eventservice.domain.entity.TicketTier;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 演出服务实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final TicketTierMapper ticketTierMapper;
    private final ObjectMapper objectMapper;

    @Override
    public EventPageResponse getEventPage(EventQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();

        // 关键词搜索（演出名称、艺人）
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(Event::getName, request.getKeyword())
                    .or()
                    .like(Event::getArtist, request.getKeyword()));
        }

        // 城市筛选
        if (StringUtils.hasText(request.getCity())) {
            queryWrapper.eq(Event::getCity, request.getCity());
        }

        // 类型筛选
        if (StringUtils.hasText(request.getType())) {
            queryWrapper.eq(Event::getType, request.getType());
        }

        // 状态筛选
        if (StringUtils.hasText(request.getStatus())) {
            queryWrapper.eq(Event::getStatus, request.getStatus());
        }

        // 按创建时间倒序排序
        queryWrapper.orderByDesc(Event::getCreatedAt);

        // 分页查询
        Page<Event> page = new Page<>(request.getPage(), request.getPageSize());
        IPage<Event> eventPage = eventMapper.selectPage(page, queryWrapper);

        // 转换为DTO
        List<EventResponse> eventResponseList = eventPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return EventPageResponse.builder()
                .list(eventResponseList)
                .total(eventPage.getTotal())
                .page(request.getPage())
                .pageSize(request.getPageSize())
                .build();
    }

    @Override
    public EventResponse getEventById(Long id) {
        Event event = eventMapper.selectById(id);
        if (event == null) {
            throw new BusinessException(404, "演出不存在");
        }
        return convertToResponse(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createEvent(EventCreateRequest request) {
        // 转换为实体（排除需要特殊处理的字段）
        Event event = new Event();
        BeanUtils.copyProperties(request, event, "images", "tags", "ticketTiers", "tips", "refundPolicy", "duration");

        // 处理图片URL（逗号分隔的字符串转为JSON数组）
        if (StringUtils.hasText(request.getImages())) {
            List<String> imageList = List.of(request.getImages().split(","));
            try {
                event.setImages(objectMapper.writeValueAsString(imageList));
            } catch (Exception e) {
                log.error("图片URL转换失败", e);
                throw new BusinessException(500, "图片数据格式错误");
            }
        }

        // 处理标签数组（转为JSON字符串）
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            try {
                event.setTags(objectMapper.writeValueAsString(request.getTags()));
            } catch (Exception e) {
                log.error("标签转换失败", e);
                throw new BusinessException(500, "标签数据格式错误");
            }
        }

        // 处理metadata（包含duration、tips、refundPolicy）
        Map<String, Object> metadata = new HashMap<>();
        if (request.getDuration() != null) {
            metadata.put("duration", request.getDuration());
        }
        if (StringUtils.hasText(request.getTips())) {
            metadata.put("tips", request.getTips());
        }
        if (StringUtils.hasText(request.getRefundPolicy())) {
            metadata.put("refund_policy", request.getRefundPolicy());
        }
        if (!metadata.isEmpty()) {
            try {
                event.setMetadata(objectMapper.writeValueAsString(metadata));
            } catch (Exception e) {
                log.error("metadata转换失败", e);
                throw new BusinessException(500, "扩展数据格式错误");
            }
        }

        // 保存演出
        eventMapper.insert(event);

        // 保存票档
        if (request.getTicketTiers() != null && !request.getTicketTiers().isEmpty()) {
            saveTicketTiers(event.getId(), request.getTicketTiers());
        }

        return event.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEvent(Long id, EventUpdateRequest request) {
        // 检查演出是否存在
        Event existingEvent = eventMapper.selectById(id);
        if (existingEvent == null) {
            throw new BusinessException(404, "演出不存在");
        }

        // 更新字段（排除需要特殊处理的字段）
        BeanUtils.copyProperties(request, existingEvent, "images", "tags", "ticketTiers", "tips", "refundPolicy", "duration");

        // 处理图片URL
        if (StringUtils.hasText(request.getImages())) {
            List<String> imageList = List.of(request.getImages().split(","));
            try {
                existingEvent.setImages(objectMapper.writeValueAsString(imageList));
            } catch (Exception e) {
                log.error("图片URL转换失败", e);
                throw new BusinessException(500, "图片数据格式错误");
            }
        }

        // 处理标签数组
        if (request.getTags() != null) {
            try {
                existingEvent.setTags(objectMapper.writeValueAsString(request.getTags()));
            } catch (Exception e) {
                log.error("标签转换失败", e);
                throw new BusinessException(500, "标签数据格式错误");
            }
        }

        // 处理metadata
        Map<String, Object> metadata = new HashMap<>();
        if (request.getDuration() != null) {
            metadata.put("duration", request.getDuration());
        }
        if (StringUtils.hasText(request.getTips())) {
            metadata.put("tips", request.getTips());
        }
        if (StringUtils.hasText(request.getRefundPolicy())) {
            metadata.put("refund_policy", request.getRefundPolicy());
        }
        if (!metadata.isEmpty()) {
            try {
                existingEvent.setMetadata(objectMapper.writeValueAsString(metadata));
            } catch (Exception e) {
                log.error("metadata转换失败", e);
                throw new BusinessException(500, "扩展数据格式错误");
            }
        }

        // 清空 updatedAt，让 MyBatis-Plus 自动填充
        existingEvent.setUpdatedAt(null);

        // 保存更新演出
        eventMapper.updateById(existingEvent);

        // 更新票档（删除旧票档，创建新票档）
        if (request.getTicketTiers() != null) {
            LambdaQueryWrapper<TicketTier> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(TicketTier::getEventId, id);
            ticketTierMapper.delete(deleteWrapper);
            if (!request.getTicketTiers().isEmpty()) {
                saveTicketTiers(id, request.getTicketTiers());
            }
        }
    }

    @Override
    public void deleteEvent(Long id) {
        // 检查演出是否存在
        Event event = eventMapper.selectById(id);
        if (event == null) {
            throw new BusinessException(404, "演出不存在");
        }

        // 删除票档
        LambdaQueryWrapper<TicketTier> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(TicketTier::getEventId, id);
        ticketTierMapper.delete(deleteWrapper);

        // 删除演出
        eventMapper.deleteById(id);
    }

    @Override
    public void updateEventStatus(Long id, String status) {
        // 检查演出是否存在
        Event event = eventMapper.selectById(id);
        if (event == null) {
            throw new BusinessException(404, "演出不存在");
        }

        // 校验状态值
        if (!StringUtils.hasText(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        // 更新状态
        event.setStatus(status);
        event.setUpdatedAt(null);
        eventMapper.updateById(event);
    }

    @Override
    public BigDecimal getMinPrice(Long eventId) {
        LambdaQueryWrapper<TicketTier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TicketTier::getEventId, eventId);
        queryWrapper.eq(TicketTier::getIsActive, true);
        queryWrapper.orderByAsc(TicketTier::getPrice);
        queryWrapper.last("LIMIT 1");

        TicketTier tier = ticketTierMapper.selectOne(queryWrapper);
        return tier != null ? tier.getPrice() : BigDecimal.ZERO;
    }

    /**
     * 转换为响应DTO
     */
    @Override
    public EventResponse convertToResponse(Event event) {
        EventResponse response = new EventResponse();
        BeanUtils.copyProperties(event, response);

        // 处理图片URL（JSON字符串转为List）
        if (StringUtils.hasText(event.getImages())) {
            try {
                response.setImages(objectMapper.readValue(event.getImages(), new TypeReference<List<String>>() {}));
            } catch (Exception e) {
                log.error("图片数据解析失败", e);
                response.setImages(new ArrayList<>());
            }
        }

        // 处理标签（JSON字符串转为List）
        if (StringUtils.hasText(event.getTags())) {
            try {
                response.setTags(objectMapper.readValue(event.getTags(), new TypeReference<List<String>>() {}));
            } catch (Exception e) {
                log.error("标签数据解析失败", e);
                response.setTags(new ArrayList<>());
            }
        }

        // 处理metadata（解析出duration、tips、refundPolicy）
        if (StringUtils.hasText(event.getMetadata())) {
            try {
                JsonNode jsonNode = objectMapper.readTree(event.getMetadata());
                if (jsonNode.has("duration")) {
                    response.setDuration(jsonNode.get("duration").asInt());
                }
                if (jsonNode.has("tips")) {
                    response.setTips(jsonNode.get("tips").asText());
                }
                if (jsonNode.has("refund_policy")) {
                    response.setRefundPolicy(jsonNode.get("refund_policy").asText());
                }
            } catch (Exception e) {
                log.error("metadata解析失败", e);
            }
        }

        // 查询并设置票档列表
        if (event.getId() != null) {
            LambdaQueryWrapper<TicketTier> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TicketTier::getEventId, event.getId());
            queryWrapper.orderByAsc(TicketTier::getSortOrder);
            List<TicketTier> ticketTiers = ticketTierMapper.selectList(queryWrapper);
            response.setTicketTiers(convertToTicketTierDTOList(ticketTiers));
        }

        return response;
    }

    /**
     * 保存票档列表
     */
    private void saveTicketTiers(Long eventId, List<EventCreateRequest.TicketTierDTO> ticketTierDTOs) {
        for (int i = 0; i < ticketTierDTOs.size(); i++) {
            EventCreateRequest.TicketTierDTO dto = ticketTierDTOs.get(i);

            TicketTier ticketTier = new TicketTier();
            ticketTier.setEventId(eventId);
            ticketTier.setName(dto.getName());
            ticketTier.setPrice(new BigDecimal(dto.getPrice().toString()));
            ticketTier.setColor(dto.getColor());
            ticketTier.setDescription(dto.getDescription());
            ticketTier.setMaxPurchase(dto.getMaxPurchase());
            ticketTier.setSortOrder(i);
            ticketTier.setIsActive(true);

            ticketTierMapper.insert(ticketTier);
        }
    }

    /**
     * 转换票档实体列表为DTO列表
     */
    private List<EventResponse.TicketTierDTO> convertToTicketTierDTOList(List<TicketTier> ticketTiers) {
        return ticketTiers.stream()
                .map(tier -> EventResponse.TicketTierDTO.builder()
                        .id(tier.getId())
                        .name(tier.getName())
                        .price(tier.getPrice().intValue())
                        .color(tier.getColor())
                        .maxPurchase(tier.getMaxPurchase())
                        .description(tier.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
