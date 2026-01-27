package com.duanyan.taopiaopiao.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duanyan.taopiaopiao.domain.entity.Venue;
import org.apache.ibatis.annotations.Mapper;

/**
 * 场馆Mapper
 *
 * @author duanyan
 * @since 1.0.0
 */
@Mapper
public interface VenueMapper extends BaseMapper<Venue> {
}
