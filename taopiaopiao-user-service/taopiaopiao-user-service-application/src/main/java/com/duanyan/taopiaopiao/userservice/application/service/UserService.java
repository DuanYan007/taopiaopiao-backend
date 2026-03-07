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


}
