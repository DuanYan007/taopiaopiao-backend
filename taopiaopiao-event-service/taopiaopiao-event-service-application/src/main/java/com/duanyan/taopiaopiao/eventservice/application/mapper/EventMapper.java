package com.duanyan.taopiaopiao.eventservice.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duanyan.taopiaopiao.eventservice.domain.entity.Event;
import org.apache.ibatis.annotations.Mapper;

/**
 * 演出Mapper
 *
 * @author duanyan
 * @since 1.0.0
 */
@Mapper
public interface EventMapper extends BaseMapper<Event> {
}
