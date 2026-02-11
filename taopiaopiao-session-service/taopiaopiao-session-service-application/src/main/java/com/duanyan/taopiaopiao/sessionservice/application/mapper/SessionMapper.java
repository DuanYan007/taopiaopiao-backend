package com.duanyan.taopiaopiao.sessionservice.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duanyan.taopiaopiao.sessionservice.domain.entity.Session;
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
