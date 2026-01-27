package com.duanyan.taopiaopiao.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.duanyan.taopiaopiao.domain.entity.AdminUser;
import com.duanyan.taopiaopiao.domain.repository.AdminUserRepository;
import com.duanyan.taopiaopiao.infrastructure.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 管理员用户仓储实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class AdminUserRepositoryImpl implements AdminUserRepository {

    private final AdminUserMapper adminUserMapper;

    @Override
    public AdminUser findById(Long id) {
        return adminUserMapper.selectById(id);
    }

    @Override
    public AdminUser findByUsername(String username) {
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUser::getUsername, username);
        return adminUserMapper.selectOne(wrapper);
    }

    @Override
    public AdminUser save(AdminUser adminUser) {
        adminUserMapper.insert(adminUser);
        return adminUser;
    }

    @Override
    public boolean update(AdminUser adminUser) {
        return adminUserMapper.updateById(adminUser) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return adminUserMapper.deleteById(id) > 0;
    }
}
