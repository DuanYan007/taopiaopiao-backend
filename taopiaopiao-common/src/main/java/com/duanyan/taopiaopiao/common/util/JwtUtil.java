package com.duanyan.taopiaopiao.common.util;

import com.duanyan.taopiaopiao.common.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT工具类
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@Component
public class JwtUtil {

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 生成密钥
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT Token
     */
    public String generateToken(Long userId, String username) {
        // Token唯一ID (用于黑名单)
        String jti = UUID.randomUUID().toString();

        // 当前时间
        Date now = new Date();

        // 过期时间
        Date expiration = new Date(now.getTime() + jwtConfig.getExpiration() * 1000);

        // 构建Token
        return Jwts.builder()
                .claim("sub", userId.toString())         // 用户ID
                .claim("username", username)             // 用户名
                .id(jti)                                 // Token唯一ID
                .issuedAt(now)                          // 签发时间
                .expiration(expiration)                  // 过期时间
                .signWith(getSignKey())                  // 签名
                .compact();
    }

    /**
     * 从Token中获取Claims
     *
     * @param token JWT Token
     * @return Claims
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token JWT Token
     * @return 用户ID
     */
    // TODO: 这里感觉有问题
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 从Token中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }

    /**
     * 从Token中获取JTI (Token唯一ID)
     *
     * @param token JWT Token
     * @return JTI
     */
    public String getJti(String token) {
        Claims claims = getClaims(token);
        return claims.getId();
    }

    /**
     * 验证Token是否过期
     *
     * @param token JWT Token
     * @return true=未过期, false=已过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            return true;
        }
    }

    /**
     * 验证Token是否有效
     *
     * @param token JWT Token
     * @return true=有效, false=无效
     */
    public boolean validateToken(String token) {
        try {
            // 检查Token格式
            if (token == null || token.isEmpty()) {
                return false;
            }

            // 检查是否过期
            if (isTokenExpired(token)) {
                return false;
            }

            // 解析Token (如果抛异常则无效)
            getClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取Token过期时间
     *
     * @param token JWT Token
     * @return 过期时间
     */
    public Date getExpiration(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    /**
     * 获取Token剩余有效时间(秒)
     *
     * @param token JWT Token
     * @return 剩余秒数
     */
    public Long getRemainingSeconds(String token) {
        Date expiration = getExpiration(token);
        long remaining = expiration.getTime() - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }
}
