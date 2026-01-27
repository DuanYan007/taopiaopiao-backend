package com.duanyan.taopiaopiao.domain.repository;

import com.duanyan.taopiaopiao.domain.entity.TicketTier;

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
}
