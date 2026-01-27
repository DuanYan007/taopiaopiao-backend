package com.duanyan.taopiaopiao.infrastructure.repository;

import com.duanyan.taopiaopiao.domain.entity.TicketTier;
import com.duanyan.taopiaopiao.domain.repository.TicketTierRepository;
import com.duanyan.taopiaopiao.infrastructure.mapper.TicketTierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 票档仓储实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class TicketTierRepositoryImpl implements TicketTierRepository {

    private final TicketTierMapper ticketTierMapper;

    @Override
    public TicketTier findById(Long id) {
        return ticketTierMapper.selectById(id);
    }

    @Override
    public TicketTier save(TicketTier ticketTier) {
        ticketTierMapper.insert(ticketTier);
        return ticketTier;
    }

    @Override
    public boolean update(TicketTier ticketTier) {
        return ticketTierMapper.updateById(ticketTier) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return ticketTierMapper.deleteById(id) > 0;
    }
}
