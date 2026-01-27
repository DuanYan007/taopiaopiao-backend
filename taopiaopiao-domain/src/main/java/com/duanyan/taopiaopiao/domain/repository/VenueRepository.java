package com.duanyan.taopiaopiao.domain.repository;

import com.duanyan.taopiaopiao.domain.entity.Venue;

/**
 * 场馆仓储接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface VenueRepository {

    /**
     * 根据ID查询
     */
    Venue findById(Long id);

    /**
     * 保存
     */
    Venue save(Venue venue);

    /**
     * 更新
     */
    boolean update(Venue venue);

    /**
     * 删除
     */
    boolean deleteById(Long id);
}
