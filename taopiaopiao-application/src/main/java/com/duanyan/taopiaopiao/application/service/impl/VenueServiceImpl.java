package com.duanyan.taopiaopiao.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.common.exception.BusinessException;
import com.duanyan.taopiaopiao.domain.dto.VenueCreateRequest;
import com.duanyan.taopiaopiao.domain.dto.VenuePageResponse;
import com.duanyan.taopiaopiao.domain.dto.VenueQueryRequest;
import com.duanyan.taopiaopiao.domain.dto.VenueResponse;
import com.duanyan.taopiaopiao.domain.dto.VenueUpdateRequest;
import com.duanyan.taopiaopiao.domain.entity.Venue;
import com.duanyan.taopiaopiao.domain.repository.VenueRepository;
import com.duanyan.taopiaopiao.application.service.VenueService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 场馆服务实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final ObjectMapper objectMapper;

    @Override
    public VenuePageResponse getVenuePage(VenueQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Venue> queryWrapper = new LambdaQueryWrapper<>();

        // 场馆名称模糊搜索
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.like(Venue::getName, request.getKeyword());
        }

        // 城市筛选
        if (StringUtils.hasText(request.getCity())) {
            queryWrapper.eq(Venue::getCity, request.getCity());
        }

        // 按创建时间倒序排序
        queryWrapper.orderByDesc(Venue::getCreatedAt);

        // 分页查询
        Page<Venue> page = new Page<>(request.getPage(), request.getPageSize());
        IPage<Venue> venuePage = venueRepository.page(page, queryWrapper);

        // 转换为DTO
        List<VenueResponse> venueResponseList = venuePage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return VenuePageResponse.builder()
                .list(venueResponseList)
                .total(venuePage.getTotal())
                .page(request.getPage())
                .pageSize(request.getPageSize())
                .build();
    }

    @Override
    public VenueResponse getVenueById(Long id) {
        Venue venue = venueRepository.findById(id);
        if (venue == null) {
            throw new BusinessException(404, "场馆不存在");
        }
        return convertToResponse(venue);
    }

    @Override
    public Long createVenue(VenueCreateRequest request) {
        // 检查场馆名称是否已存在
        LambdaQueryWrapper<Venue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Venue::getName, request.getName());
        if (venueRepository.findOne(queryWrapper) != null) {
            throw new BusinessException(400, "场馆名称已存在");
        }

        // 转换为实体
        Venue venue = new Venue();
        BeanUtils.copyProperties(request, venue);

        // 处理设施数组（转为JSON字符串）
        if (request.getFacilities() != null && !request.getFacilities().isEmpty()) {
            try {
                venue.setFacilities(objectMapper.writeValueAsString(request.getFacilities()));
            } catch (Exception e) {
                log.error("设施数组转换失败", e);
                throw new BusinessException(500, "设施数据格式错误");
            }
        }

        // 处理图片URL（逗号分隔的字符串转为JSON数组）
        if (StringUtils.hasText(request.getImages())) {
            List<String> imageList = Arrays.asList(request.getImages().split(","));
            try {
                venue.setImages(objectMapper.writeValueAsString(imageList));
            } catch (Exception e) {
                log.error("图片URL转换失败", e);
                throw new BusinessException(500, "图片数据格式错误");
            }
        }

        // 保存
        venueRepository.save(venue);
        return venue.getId();
    }

    @Override
    public void updateVenue(Long id, VenueUpdateRequest request) {
        // 检查场馆是否存在
        Venue existingVenue = venueRepository.findById(id);
        if (existingVenue == null) {
            throw new BusinessException(404, "场馆不存在");
        }

        // 检查场馆名称是否与其他场馆重复
        LambdaQueryWrapper<Venue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Venue::getName, request.getName());
        queryWrapper.ne(Venue::getId, id);
        if (venueRepository.findOne(queryWrapper) != null) {
            throw new BusinessException(400, "场馆名称已存在");
        }

        // 更新字段
        BeanUtils.copyProperties(request, existingVenue);

        // 处理设施数组
        if (request.getFacilities() != null && !request.getFacilities().isEmpty()) {
            try {
                existingVenue.setFacilities(objectMapper.writeValueAsString(request.getFacilities()));
            } catch (Exception e) {
                log.error("设施数组转换失败", e);
                throw new BusinessException(500, "设施数据格式错误");
            }
        }

        // 处理图片URL
        if (StringUtils.hasText(request.getImages())) {
            List<String> imageList = Arrays.asList(request.getImages().split(","));
            try {
                existingVenue.setImages(objectMapper.writeValueAsString(imageList));
            } catch (Exception e) {
                log.error("图片URL转换失败", e);
                throw new BusinessException(500, "图片数据格式错误");
            }
        }

        // 保存更新
        venueRepository.save(existingVenue);
    }

    @Override
    public void deleteVenue(Long id) {
        // 检查场馆是否存在
        Venue venue = venueRepository.findById(id);
        if (venue == null) {
            throw new BusinessException(404, "场馆不存在");
        }

        // 删除
        venueRepository.deleteById(id);
    }

    /**
     * 转换为响应DTO
     */
    private VenueResponse convertToResponse(Venue venue) {
        VenueResponse response = new VenueResponse();
        BeanUtils.copyProperties(venue, response);

        // 处理设施数组（JSON字符串转为List）
        if (StringUtils.hasText(venue.getFacilities())) {
            try {
                response.setFacilities(objectMapper.readValue(venue.getFacilities(), new TypeReference<List<String>>() {}));
            } catch (Exception e) {
                log.error("设施数据解析失败", e);
                response.setFacilities(new ArrayList<>());
            }
        }

        // 处理图片URL（JSON字符串转为List）
        if (StringUtils.hasText(venue.getImages())) {
            try {
                response.setImages(objectMapper.readValue(venue.getImages(), new TypeReference<List<String>>() {}));
            } catch (Exception e) {
                log.error("图片数据解析失败", e);
                response.setImages(new ArrayList<>());
            }
        }

        return response;
    }
}
