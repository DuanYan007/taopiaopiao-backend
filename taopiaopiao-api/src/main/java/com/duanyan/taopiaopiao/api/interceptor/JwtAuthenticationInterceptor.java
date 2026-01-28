package com.duanyan.taopiaopiao.api.interceptor;

import com.duanyan.taopiaopiao.common.config.JwtConfig;
import com.duanyan.taopiaopiao.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT认证拦截器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取请求路径
        String requestURI = request.getRequestURI();
        log.debug("请求路径: {}", requestURI);

        // 登录接口放行 (Nginx代理后: /admin/auth/login)
        // TODO: 这句是没用的
//        if (requestURI.equals("/admin/auth/login") || requestURI.startsWith("/admin/auth/")) {
//            return true;
//        }

        // 获取Authorization头
        String authorization = request.getHeader(jwtConfig.getHeaderName());
        log.info("收到请求 - URI: {}, Authorization头: {}", requestURI, authorization);

        // 检查Token是否存在
        if (authorization == null || authorization.trim().isEmpty()) {
            log.warn("认证失败 - Authorization为空");
            sendErrorResponse(response, 401, "未提供认证令牌");
            return false;
        }

        // 提取Token（支持带或不带Bearer前缀）
        String token;
        if (authorization.startsWith(jwtConfig.getTokenPrefix())) {
            // 包含 Bearer 前缀，去除前缀
            token = authorization.substring(jwtConfig.getTokenPrefix().length());
        } else {
            // 不包含前缀，直接使用
            token = authorization;
            log.debug("Token不包含Bearer前缀，直接使用");
        }

        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            sendErrorResponse(response, 401, "登录已过期");
            return false;
        }

        // TODO: 检查Token是否在黑名单(需要Redis)
        // String jti = jwtUtil.getJti(token);
        // if (tokenBlacklist.isBlacklisted(jti)) {
        //     sendErrorResponse(response, 401, "令牌已失效");
        //     return false;
        // }

        // Token验证通过,将用户信息存入请求属性
        // TODO:
        Long userId = jwtUtil.getUserId(token);
        String username = jwtUtil.getUsername(token);
        request.setAttribute("userId", userId);
        request.setAttribute("username", username);

        log.debug("用户认证通过: userId={}, username={}", userId, username);

        // 放行
        return true;
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String jsonResponse = String.format("{\"code\": %d, \"message\": \"%s\"}", code, message);
        response.getWriter().write(jsonResponse);
    }
}
