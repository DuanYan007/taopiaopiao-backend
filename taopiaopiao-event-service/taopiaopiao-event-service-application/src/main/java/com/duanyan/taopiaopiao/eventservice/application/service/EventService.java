package com.duanyan.taopiaopiao.eventservice.application.service;

import com.duanyan.taopiaopiao.eventservice.api.dto.EventCreateRequest;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventPageResponse;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventQueryRequest;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventResponse;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventUpdateRequest;

/**
 * 演出服务接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface EventService {

    /**
     * 分页查询演出列表
     *
     * @param request 查询请求
     * @return 分页响应
     */
    EventPageResponse getEventPage(EventQueryRequest request);

    /**
     * 根据ID查询演出详情
     *
     * @param id 演出ID
     * @return 演出详情
     */
    EventResponse getEventById(Long id);

    /**
     * 创建演出
     *
     * @param request 创建请求
     * @return 演出ID
     */
    Long createEvent(EventCreateRequest request);

    /**
     * 更新演出
     *
     * @param id 演出ID
     * @param request 更新请求
     */
    void updateEvent(Long id, EventUpdateRequest request);

    /**
     * 删除演出
     *
     * @param id 演出ID
     */
    void deleteEvent(Long id);
}
