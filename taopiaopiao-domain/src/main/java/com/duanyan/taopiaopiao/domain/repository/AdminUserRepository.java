package com.duanyan.taopiaopiao.domain.repository;

import com.duanyan.taopiaopiao.domain.entity.AdminUser;

/**
 * 管理员用户仓储接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface AdminUserRepository {

    /**
     * 根据ID查询
     */
    AdminUser findById(Long id);

    /**
     * 根据用户名查询
     */
    AdminUser findByUsername(String username);

    /**
     * 保存
     */
    AdminUser save(AdminUser adminUser);

    /**
     * 更新
     */
    boolean update(AdminUser adminUser);

    /**
     * 删除
     */
    boolean deleteById(Long id);
}
