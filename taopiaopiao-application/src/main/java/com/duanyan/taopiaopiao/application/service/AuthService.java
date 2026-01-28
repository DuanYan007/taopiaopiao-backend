package com.duanyan.taopiaopiao.application.service;

import com.duanyan.taopiaopiao.domain.dto.LoginRequest;
import com.duanyan.taopiaopiao.domain.dto.LoginResponse;

/**
 * 认证服务接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface AuthService {

    /**
     * 管理员登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);

    /**
     * 管理员登出
     *
     * @param token JWT Token
     */
    void logout(String token);
}
