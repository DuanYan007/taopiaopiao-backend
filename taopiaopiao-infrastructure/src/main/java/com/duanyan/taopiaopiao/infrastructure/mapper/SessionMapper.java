package com.duanyan.taopiaopiao.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duanyan.taopiaopiao.domain.entity.Session;
import org.apache.ibatis.annotations.Mapper;

/**
 * 场次Mapper
 *
 * @author duanyan
 * @since 1.0.0
 */
@Mapper
public interface SessionMapper extends BaseMapper<Session> {
}
