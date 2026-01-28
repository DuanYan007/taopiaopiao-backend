package com.duanyan.taopiaopiao.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duanyan.taopiaopiao.domain.entity.Venue;

/**
 * 场馆仓储接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface VenueRepository {

    /**
     * 根据ID查询
     */
    Venue findById(Long id);

    /**
     * 保存
     */
    Venue save(Venue venue);

    /**
     * 更新
     */
    boolean update(Venue venue);

    /**
     * 删除
     */
    boolean deleteById(Long id);

    /**
     * 分页查询
     */
    IPage<Venue> page(Page<Venue> page, LambdaQueryWrapper<Venue> queryWrapper);

    /**
     * 根据条件查询单个对象
     */
    Venue findOne(LambdaQueryWrapper<Venue> queryWrapper);
}
