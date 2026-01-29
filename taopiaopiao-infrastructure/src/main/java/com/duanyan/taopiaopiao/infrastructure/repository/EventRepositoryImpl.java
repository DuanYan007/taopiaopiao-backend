package com.duanyan.taopiaopiao.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.domain.entity.Event;
import com.duanyan.taopiaopiao.domain.repository.EventRepository;
import com.duanyan.taopiaopiao.infrastructure.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 演出仓储实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    private final EventMapper eventMapper;

    @Override
    public Event findById(Long id) {
        return eventMapper.selectById(id);
    }

    @Override
    public Event save(Event event) {
        if (event.getId() == null) {
            eventMapper.insert(event);
        } else {
            eventMapper.updateById(event);
        }
        return event;
    }

    @Override
    public boolean update(Event event) {
        return eventMapper.updateById(event) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return eventMapper.deleteById(id) > 0;
    }

    @Override
    public IPage<Event> page(Page<Event> page, LambdaQueryWrapper<Event> queryWrapper) {
        return eventMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Event findOne(LambdaQueryWrapper<Event> queryWrapper) {
        return eventMapper.selectOne(queryWrapper);
    }
}
