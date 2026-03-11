package com.duanyan.taopiaopiao.eventservice.application.service;

import com.duanyan.taopiaopiao.eventservice.api.dto.EventPageResponse;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventQueryRequest;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventResponse;
import com.duanyan.taopiaopiao.eventservice.api.dto.SessionBriefPageResponse;

/**
 * 演出客户端服务接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface ClientEventService {

    /**
     * 分页查询演出列表（客户端）
     *
     * @param request 查询请求
     * @return 分页响应
     */
    EventPageResponse getEventPage(EventQueryRequest request);

    /**
     * 根据ID查询演出详情（客户端）
     *
     * @param id 演出ID
     * @return 演出详情
     */
    EventResponse getEventById(Long id);

    /**
     * 查询演出的场次列表（客户端）
     *
     * @param eventId 演出ID
     * @param status  状态筛选
     * @param page    页码
     * @param pageSize 每页条数
     * @return 场次分页响应
     */
    SessionBriefPageResponse getEventSessions(Long eventId, String status, Integer page, Integer pageSize);
}
