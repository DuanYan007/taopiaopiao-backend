package com.duanyan.taopiaopiao.domain.repository;

import com.duanyan.taopiaopiao.domain.entity.Seat;

/**
 * 座位仓储接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface SeatRepository {

    /**
     * 根据ID查询
     */
    Seat findById(Long id);

    /**
     * 保存
     */
    Seat save(Seat seat);

    /**
     * 更新
     */
    boolean update(Seat seat);

    /**
     * 删除
     */
    boolean deleteById(Long id);
}
