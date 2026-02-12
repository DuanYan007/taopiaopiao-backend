package com.duanyan.taopiaopiao.userservice.application.controller;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.userservice.api.dto.LoginRequest;
import com.duanyan.taopiaopiao.userservice.api.dto.LoginResponse;
import com.duanyan.taopiaopiao.userservice.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "登录、登出接口")
public class AuthController {

    private final UserService userService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Result.success(response);
    }

    /**
     * 管理员登出
     */
    @PostMapping("/logout")
    @Operation(summary = "管理员登出")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        log.info("用户登出成功, token: {}", token);
        return Result.success();
    }
}
