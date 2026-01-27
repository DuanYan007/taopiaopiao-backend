package com.duanyan.taopiaopiao.infrastructure.repository;

import com.duanyan.taopiaopiao.domain.entity.Seat;
import com.duanyan.taopiaopiao.domain.repository.SeatRepository;
import com.duanyan.taopiaopiao.infrastructure.mapper.SeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 座位仓储实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatMapper seatMapper;

    @Override
    public Seat findById(Long id) {
        return seatMapper.selectById(id);
    }

    @Override
    public Seat save(Seat seat) {
        seatMapper.insert(seat);
        return seat;
    }

    @Override
    public boolean update(Seat seat) {
        return seatMapper.updateById(seat) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return seatMapper.deleteById(id) > 0;
    }
}
