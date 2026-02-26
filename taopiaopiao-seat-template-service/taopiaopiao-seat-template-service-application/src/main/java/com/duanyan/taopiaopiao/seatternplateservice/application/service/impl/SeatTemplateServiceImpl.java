package com.duanyan.taopiaopiao.seatternplateservice.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.common.exception.BusinessException;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.*;
import com.duanyan.taopiaopiao.seatternplateservice.application.client.VenueClient;
import com.duanyan.taopiaopiao.seatternplateservice.application.mapper.SeatTemplateMapper;
import com.duanyan.taopiaopiao.seatternplateservice.application.service.SeatTemplateService;
import com.duanyan.taopiaopiao.seatternplateservice.domain.entity.SeatTemplate;
import com.duanyan.taopiaopiao.seatternplateservice.domain.enums.LayoutTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 座位模板服务实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeatTemplateServiceImpl implements SeatTemplateService {

    private final SeatTemplateMapper seatTemplateMapper;
    private final VenueClient venueClient;

    @Override
    public SeatTemplatePageResponse getTemplatePage(SeatTemplateQueryRequest request) {
        Page<SeatTemplate> page = new Page<>(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<SeatTemplate> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getName())) {
            queryWrapper.like(SeatTemplate::getName, request.getName());
        }
        if (request.getVenueId() != null) {
            queryWrapper.eq(SeatTemplate::getVenueId, request.getVenueId());
        }
        if (StringUtils.hasText(request.getTemplateCode())) {
            queryWrapper.eq(SeatTemplate::getTemplateCode, request.getTemplateCode());
        }
        if (request.getLayoutType() != null) {
            queryWrapper.eq(SeatTemplate::getLayoutType, request.getLayoutType());
        }
        if (request.getStatus() != null) {
            queryWrapper.eq(SeatTemplate::getStatus, request.getStatus());
        }
        queryWrapper.orderByDesc(SeatTemplate::getCreatedAt);

        Page<SeatTemplate> resultPage = seatTemplateMapper.selectPage(page, queryWrapper);

        // 收集需要查询的场馆ID
        List<Long> venueIds = resultPage.getRecords().stream()
                .map(SeatTemplate::getVenueId)
                .filter(id -> id != null)
                .distinct()
                .toList();

        // 批量获取场馆信息
        Map<Long, VenueInfo> venueMap = fetchVenuesByIds(venueIds);

        // 转换为响应对象并填充场馆名称
        List<SeatTemplateResponse> records = resultPage.getRecords().stream()
                .map(template -> convertToResponse(template, venueMap))
                .collect(Collectors.toList());

        SeatTemplatePageResponse response = new SeatTemplatePageResponse();
        response.setTotal(resultPage.getTotal());
        response.setPageNum(request.getPageNum());
        response.setPageSize(request.getPageSize());
        response.setRecords(records);
        return response;
    }

    @Override
    public SeatTemplateResponse getTemplateById(Long id) {
        SeatTemplate seatTemplate = seatTemplateMapper.selectById(id);
        if (seatTemplate == null) {
            throw new BusinessException("座位模板不存在");
        }

        // 查询场馆信息
        Map<Long, VenueInfo> venueMap = fetchVenuesByIds(List.of(seatTemplate.getVenueId()));

        return convertToResponse(seatTemplate, venueMap);
    }

    @Override
    public Long createTemplate(SeatTemplateCreateRequest request) {
        // 检查模板编码是否已存在
        LambdaQueryWrapper<SeatTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SeatTemplate::getTemplateCode, request.getTemplateCode());
        Long count = seatTemplateMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException("模板编码已存在");
        }

        // 验证场馆是否存在
        if (request.getVenueId() != null) {
            Result<VenueInfo> venueResult = venueClient.getVenueById(request.getVenueId());
            if (venueResult == null || venueResult.getData() == null) {
                throw new BusinessException("场馆不存在");
            }
        }

        SeatTemplate seatTemplate = new SeatTemplate();
        BeanUtils.copyProperties(request, seatTemplate);
        seatTemplate.setStatus(1); // 默认启用

        seatTemplateMapper.insert(seatTemplate);
        return seatTemplate.getId();
    }

    @Override
    public void updateTemplate(Long id, SeatTemplateUpdateRequest request) {
        SeatTemplate existingTemplate = seatTemplateMapper.selectById(id);
        if (existingTemplate == null) {
            throw new BusinessException("座位模板不存在");
        }

        if (StringUtils.hasText(request.getName())) {
            existingTemplate.setName(request.getName());
        }
        if (request.getTotalRows() != null) {
            existingTemplate.setTotalRows(request.getTotalRows());
        }
        if (request.getTotalSeats() != null) {
            existingTemplate.setTotalSeats(request.getTotalSeats());
        }
        if (request.getLayoutType() != null) {
            existingTemplate.setLayoutType(request.getLayoutType());
        }
        if (StringUtils.hasText(request.getLayoutData())) {
            existingTemplate.setLayoutData(request.getLayoutData());
        }
        if (request.getStatus() != null) {
            existingTemplate.setStatus(request.getStatus());
        }

        // 清空 updatedAt 让自动填充生效
        existingTemplate.setUpdatedAt(null);

        seatTemplateMapper.updateById(existingTemplate);
    }

    @Override
    public void deleteTemplate(Long id) {
        SeatTemplate seatTemplate = seatTemplateMapper.selectById(id);
        if (seatTemplate == null) {
            throw new BusinessException("座位模板不存在");
        }
        seatTemplateMapper.deleteById(id);
    }

    @Override
    public List<SeatTemplateResponse> listByVenueId(Long venueId) {
        LambdaQueryWrapper<SeatTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SeatTemplate::getVenueId, venueId);
        queryWrapper.eq(SeatTemplate::getStatus, 1);
        queryWrapper.orderByAsc(SeatTemplate::getCreatedAt);

        List<SeatTemplate> templates = seatTemplateMapper.selectList(queryWrapper);

        // 查询场馆信息
        Map<Long, VenueInfo> venueMap = fetchVenuesByIds(List.of(venueId));

        return templates.stream()
                .map(template -> convertToResponse(template, venueMap))
                .collect(Collectors.toList());
    }

    /**
     * 批量获取场馆信息
     * 参考 session-service 的 fetchVenuesByIds 实现
     */
    private Map<Long, VenueInfo> fetchVenuesByIds(List<Long> venueIds) {
        if (venueIds == null || venueIds.isEmpty()) {
            return new HashMap<>();
        }

        Map<Long, VenueInfo> result = new HashMap<>();
        for (Long venueId : venueIds) {
            try {
                Result<VenueInfo> resp = venueClient.getVenueById(venueId);
                if (resp != null && resp.getData() != null) {
                    result.put(venueId, resp.getData());
                }
            } catch (Exception e) {
                log.error("查询场馆信息失败, venueId: {}, error: {}", venueId, e.getMessage());
            }
        }
        return result;
    }

    /**
     * 转换为响应对象
     */
    private SeatTemplateResponse convertToResponse(SeatTemplate seatTemplate) {
        return convertToResponse(seatTemplate, new HashMap<>());
    }

    /**
     * 转换为响应对象（带场馆信息）
     */
    private SeatTemplateResponse convertToResponse(SeatTemplate seatTemplate, Map<Long, VenueInfo> venueMap) {
        SeatTemplateResponse response = new SeatTemplateResponse();
        BeanUtils.copyProperties(seatTemplate, response);

        LayoutTypeEnum layoutTypeEnum = LayoutTypeEnum.fromCode(seatTemplate.getLayoutType());
        response.setLayoutTypeName(layoutTypeEnum.getDesc());

        // 从查询结果中填充场馆名称
        if (seatTemplate.getVenueId() != null && venueMap.containsKey(seatTemplate.getVenueId())) {
            response.setVenueName(venueMap.get(seatTemplate.getVenueId()).getName());
        }

        return response;
    }
}
