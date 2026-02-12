package com.duanyan.taopiaopiao.venueservice.application.service;

import com.duanyan.taopiaopiao.venueservice.api.dto.VenueCreateRequest;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenuePageResponse;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueQueryRequest;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueResponse;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueUpdateRequest;

/**
 * 场馆服务接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface VenueService {

    /**
     * 分页查询场馆列表
     *
     * @param request 查询请求
     * @return 分页响应
     */
    VenuePageResponse getVenuePage(VenueQueryRequest request);

    /**
     * 根据ID查询场馆详情
     *
     * @param id 场馆ID
     * @return 场馆详情
     */
    VenueResponse getVenueById(Long id);

    /**
     * 创建场馆
     *
     * @param request 创建请求
     * @return 场馆ID
     */
    Long createVenue(VenueCreateRequest request);

    /**
     * 更新场馆
     *
     * @param id 场馆ID
     * @param request 更新请求
     */
    void updateVenue(Long id, VenueUpdateRequest request);

    /**
     * 删除场馆
     *
     * @param id 场馆ID
     */
    void deleteVenue(Long id);
}
