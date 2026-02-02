package com.duanyan.taopiaopiao.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.domain.entity.Session;
import com.duanyan.taopiaopiao.domain.repository.SessionRepository;
import com.duanyan.taopiaopiao.infrastructure.mapper.SessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 场次仓储实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {

    private final SessionMapper sessionMapper;

    @Override
    public Session findById(Long id) {
        return sessionMapper.selectById(id);
    }

    @Override
    public Session save(Session session) {
        sessionMapper.insert(session);
        return session;
    }

    @Override
    public boolean update(Session session) {
        return sessionMapper.updateById(session) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return sessionMapper.deleteById(id) > 0;
    }

    @Override
    public IPage<Session> page(Page<Session> page, LambdaQueryWrapper<Session> queryWrapper) {
        return sessionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Session findOne(LambdaQueryWrapper<Session> queryWrapper) {
        return sessionMapper.selectOne(queryWrapper);
    }
}
