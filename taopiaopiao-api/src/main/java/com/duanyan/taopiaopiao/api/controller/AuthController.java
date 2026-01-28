package com.duanyan.taopiaopiao.api.controller;

import com.duanyan.taopiaopiao.domain.dto.LoginRequest;
import com.duanyan.taopiaopiao.domain.dto.LoginResponse;
import com.duanyan.taopiaopiao.application.service.AuthService;
import com.duanyan.taopiaopiao.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Tag(name = "认证管理", description = "登录、登出接口")
@RestController
@RequestMapping("/admin/auth")  // Nginx代理后,去掉/api前缀
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 管理员登录
     */
    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success("登录成功", response);
    }

    /**
     * 管理员登出
     */
    @Operation(summary = "管理员登出")
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String authorization) {
        // 提取Token (去除 "Bearer " 前缀)
        String token = authorization.replace("Bearer ", "");
        authService.logout(token);
        return Result.success("登出成功");
    }
}
