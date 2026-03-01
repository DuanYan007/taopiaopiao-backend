package com.duanyan.taopiaopiao.seckillservice.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duanyan.taopiaopiao.seckillservice.domain.entity.SeatLock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeatLockMapper extends BaseMapper<SeatLock> {

    int updateStatus(Long sessionId, Long userId, String seatId, Integer status);
}
