package com.duanyan.taopiaopiao.domain.repository;

import com.duanyan.taopiaopiao.domain.entity.Event;

/**
 * 演出仓储接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface EventRepository {

    /**
     * 根据ID查询
     */
    Event findById(Long id);

    /**
     * 保存
     */
    Event save(Event event);

    /**
     * 更新
     */
    boolean update(Event event);

    /**
     * 删除
     */
    boolean deleteById(Long id);
}
