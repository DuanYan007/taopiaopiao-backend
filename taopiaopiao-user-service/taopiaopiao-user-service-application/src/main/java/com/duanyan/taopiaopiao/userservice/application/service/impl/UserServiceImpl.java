package com.duanyan.taopiaopiao.userservice.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.duanyan.taopiaopiao.common.exception.BusinessException;
import com.duanyan.taopiaopiao.common.web.util.JwtUtil;
import com.duanyan.taopiaopiao.userservice.api.dto.LoginRequest;
import com.duanyan.taopiaopiao.userservice.api.dto.LoginResponse;
import com.duanyan.taopiaopiao.userservice.api.dto.UserResponse;
import com.duanyan.taopiaopiao.userservice.application.mapper.AdminUserMapper;
import com.duanyan.taopiaopiao.userservice.application.service.UserService;
import com.duanyan.taopiaopiao.userservice.domain.constant.UserConstants;
import com.duanyan.taopiaopiao.userservice.domain.entity.AdminUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AdminUserMapper adminUserMapper;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 查询用户
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUser::getUsername, request.getUsername());
        AdminUser adminUser = adminUserMapper.selectOne(queryWrapper);

        if (adminUser == null) {
            throw new BusinessException(UserConstants.ErrorCode.INVALID_CREDENTIALS, "用户名或密码错误");
        }

        // 2. 验证密码
        if (!request.getPassword().equals(adminUser.getPassword())) {
            throw new BusinessException(UserConstants.ErrorCode.INVALID_CREDENTIALS, "用户名或密码错误");
        }

        // 3. 检查用户状态
        if (!UserConstants.Status.ACTIVE.equals(adminUser.getStatus())) {
            throw new BusinessException(UserConstants.ErrorCode.ACCOUNT_DISABLED, "账号已被禁用或锁定");
        }

        // 4. 更新最后登录时间
        adminUser.setLastLoginAt(LocalDateTime.now());
        adminUserMapper.updateById(adminUser);

        // 5. 生成Token
        String token = jwtUtil.generateToken(adminUser.getId(), adminUser.getUsername());

        // 6. 构建响应
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
                .id(adminUser.getId())
                .username(adminUser.getUsername())
                .realName(adminUser.getRealName())
                .email(adminUser.getEmail())
                .build();

        return LoginResponse.builder()
                .token(token)
                .userInfo(userInfo)
                .build();
    }


}
