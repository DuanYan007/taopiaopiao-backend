package com.duanyan.taopiaopiao.application.service.impl;

import com.duanyan.taopiaopiao.domain.dto.LoginRequest;
import com.duanyan.taopiaopiao.domain.dto.LoginResponse;
import com.duanyan.taopiaopiao.common.exception.BusinessException;
import com.duanyan.taopiaopiao.common.util.JwtUtil;
import com.duanyan.taopiaopiao.domain.entity.AdminUser;
import com.duanyan.taopiaopiao.domain.repository.AdminUserRepository;
import com.duanyan.taopiaopiao.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 查询用户
        AdminUser adminUser = adminUserRepository.findByUsername(request.getUsername());
        if (adminUser == null) {
            throw new BusinessException(1001, "用户名或密码错误");
        }

        // 2. 验证密码
//        if (!passwordEncoder.matches(request.getPassword(), adminUser.getPassword())) {
//            throw new BusinessException(1001, "用户名或密码错误");
//        }
        if (!request.getPassword().equals(adminUser.getPassword())) {
            throw new BusinessException(1001, "用户名或密码错误");
        }


        // 3. 检查用户状态
        if (!"active".equals(adminUser.getStatus())) {
            throw new BusinessException(1002, "账号已被禁用或锁定");
        }

        // 4. 生成Token
        String token = jwtUtil.generateToken(adminUser.getId(), adminUser.getUsername());

        // 5. 构建响应
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

    @Override
    public void logout(String token) {
        // TODO: 实现Token黑名单(需要Redis)
        // 1. 从Token中提取JTI
        // 2. 将JTI加入黑名单
        // 3. 设置过期时间为Token剩余有效期
        log.info("用户登出成功, token: {}", token);
    }
}
