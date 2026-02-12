package com.duanyan.taopiaopiao.sessionservice.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duanyan.taopiaopiao.sessionservice.domain.entity.Seat;
import org.apache.ibatis.annotations.Mapper;

/**
 * 座位Mapper
 *
 * @author duanyan
 * @since 1.0.0
 */
@Mapper
public interface SeatMapper extends BaseMapper<Seat> {
}
