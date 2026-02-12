package com.duanyan.taopiaopiao.userservice.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duanyan.taopiaopiao.userservice.domain.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员用户Mapper
 *
 * @author duanyan
 * @since 1.0.0
 */
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
