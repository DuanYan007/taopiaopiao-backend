package com.duanyan.taopiaopiao.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.duanyan.taopiaopiao.domain.entity.TicketTier;
import com.duanyan.taopiaopiao.domain.repository.TicketTierRepository;
import com.duanyan.taopiaopiao.infrastructure.mapper.TicketTierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        if (ticketTier.getId() == null) {
            ticketTierMapper.insert(ticketTier);
        } else {
            ticketTierMapper.updateById(ticketTier);
        }
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

    @Override
    public List<TicketTier> findByEventId(Long eventId) {
        LambdaQueryWrapper<TicketTier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TicketTier::getEventId, eventId);
        queryWrapper.orderByAsc(TicketTier::getSortOrder);
        return ticketTierMapper.selectList(queryWrapper);
    }

    @Override
    public boolean deleteByEventId(Long eventId) {
        LambdaQueryWrapper<TicketTier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TicketTier::getEventId, eventId);
        return ticketTierMapper.delete(queryWrapper) >= 0;
    }

    @Override
    public TicketTier findOne(LambdaQueryWrapper<TicketTier> queryWrapper) {
        return ticketTierMapper.selectOne(queryWrapper);
    }
}
