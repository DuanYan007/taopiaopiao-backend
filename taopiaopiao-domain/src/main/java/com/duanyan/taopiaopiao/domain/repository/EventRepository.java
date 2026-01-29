package com.duanyan.taopiaopiao.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.domain.entity.Event;

/**
 * 演出仓储接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface EventRepository {

    /**
     * 根据ID查询
     */
    Event findById(Long id);

    /**
     * 保存
     */
    Event save(Event event);

    /**
     * 更新
     */
    boolean update(Event event);

    /**
     * 删除
     */
    boolean deleteById(Long id);

    /**
     * 分页查询
     */
    IPage<Event> page(Page<Event> page, LambdaQueryWrapper<Event> queryWrapper);

    /**
     * 根据条件查询单个
     */
    Event findOne(LambdaQueryWrapper<Event> queryWrapper);
}
