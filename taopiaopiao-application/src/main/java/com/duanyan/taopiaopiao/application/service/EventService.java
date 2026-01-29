package com.duanyan.taopiaopiao.application.service;

import com.duanyan.taopiaopiao.domain.dto.EventCreateRequest;
import com.duanyan.taopiaopiao.domain.dto.EventPageResponse;
import com.duanyan.taopiaopiao.domain.dto.EventQueryRequest;
import com.duanyan.taopiaopiao.domain.dto.EventResponse;
import com.duanyan.taopiaopiao.domain.dto.EventUpdateRequest;

/**
 * 演出服务接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface EventService {

    /**
     * 分页查询演出列表
     */
    EventPageResponse getEventPage(EventQueryRequest request);

    /**
     * 根据ID查询演出详情
     */
    EventResponse getEventById(Long id);

    /**
     * 创建演出
     */
    Long createEvent(EventCreateRequest request);

    /**
     * 更新演出
     */
    void updateEvent(Long id, EventUpdateRequest request);

    /**
     * 删除演出
     */
    void deleteEvent(Long id);

    /**
     * 更新演出状态
     */
    void updateEventStatus(Long id, String status);
}
