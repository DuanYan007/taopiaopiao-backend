package com.duanyan.taopiaopiao.infrastructure.repository;

import com.duanyan.taopiaopiao.domain.entity.Venue;
import com.duanyan.taopiaopiao.domain.repository.VenueRepository;
import com.duanyan.taopiaopiao.infrastructure.mapper.VenueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 场馆仓储实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class VenueRepositoryImpl implements VenueRepository {

    private final VenueMapper venueMapper;

    @Override
    public Venue findById(Long id) {
        return venueMapper.selectById(id);
    }

    @Override
    public Venue save(Venue venue) {
        venueMapper.insert(venue);
        return venue;
    }

    @Override
    public boolean update(Venue venue) {
        return venueMapper.updateById(venue) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return venueMapper.deleteById(id) > 0;
    }
}
