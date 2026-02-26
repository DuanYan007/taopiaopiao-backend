package com.duanyan.taopiaopiao.seatternplateservice.application.service;

import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplateCreateRequest;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplatePageResponse;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplateQueryRequest;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplateResponse;
import com.duanyan.taopiaopiao.seatternplateservice.api.dto.SeatTemplateUpdateRequest;

/**
 * 座位模板服务接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface SeatTemplateService {

    /**
     * 分页查询座位模板
     */
    SeatTemplatePageResponse getTemplatePage(SeatTemplateQueryRequest request);

    /**
     * 根据ID查询座位模板详情
     */
    SeatTemplateResponse getTemplateById(Long id);

    /**
     * 创建座位模板
     */
    Long createTemplate(SeatTemplateCreateRequest request);

    /**
     * 更新座位模板
     */
    void updateTemplate(Long id, SeatTemplateUpdateRequest request);

    /**
     * 删除座位模板
     */
    void deleteTemplate(Long id);

    /**
     * 根据场馆ID查询模板列表
     */
    java.util.List<SeatTemplateResponse> listByVenueId(Long venueId);
}
