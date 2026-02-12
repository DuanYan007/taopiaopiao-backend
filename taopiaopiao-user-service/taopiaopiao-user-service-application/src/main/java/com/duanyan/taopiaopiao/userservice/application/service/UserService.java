package com.duanyan.taopiaopiao.userservice.application.service;

import com.duanyan.taopiaopiao.userservice.api.dto.LoginRequest;
import com.duanyan.taopiaopiao.userservice.api.dto.LoginResponse;
import com.duanyan.taopiaopiao.userservice.api.dto.UserResponse;

/**
 * 用户服务接口
 *
 * @author duanyan
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    UserResponse getUserById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserResponse getUserByUsername(String username);

    /**
     * 验证Token
     *
     * @param token JWT Token
     * @return 用户ID
     */
    Long validateToken(String token);
}
