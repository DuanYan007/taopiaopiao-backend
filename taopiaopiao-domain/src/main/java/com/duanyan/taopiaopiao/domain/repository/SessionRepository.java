package com.duanyan.taopiaopiao.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.domain.entity.Session;

/**
 * 场次仓储接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface SessionRepository {

    /**
     * 根据ID查询
     */
    Session findById(Long id);

    /**
     * 保存
     */
    Session save(Session session);

    /**
     * 更新
     */
    boolean update(Session session);

    /**
     * 删除
     */
    boolean deleteById(Long id);

    /**
     * 分页查询
     */
    IPage<Session> page(Page<Session> page, LambdaQueryWrapper<Session> queryWrapper);

    /**
     * 根据条件查询单个
     */
    Session findOne(LambdaQueryWrapper<Session> queryWrapper);
}
