package com.duanyan.taopiaopiao.orderservice.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duanyan.taopiaopiao.orderservice.domain.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
