package com.duanyan.taopiaopiao.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.duanyan.taopiaopiao.domain.entity.TicketTier;

import java.util.List;

/**
 * 票档仓储接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface TicketTierRepository {

    /**
     * 根据ID查询
     */
    TicketTier findById(Long id);

    /**
     * 保存
     */
    TicketTier save(TicketTier ticketTier);

    /**
     * 更新
     */
    boolean update(TicketTier ticketTier);

    /**
     * 删除
     */
    boolean deleteById(Long id);

    /**
     * 根据演出ID查询所有票档
     */
    List<TicketTier> findByEventId(Long eventId);

    /**
     * 删除演出的所有票档
     */
    boolean deleteByEventId(Long eventId);

    /**
     * 根据条件查询单个
     */
    TicketTier findOne(LambdaQueryWrapper<TicketTier> queryWrapper);
}
