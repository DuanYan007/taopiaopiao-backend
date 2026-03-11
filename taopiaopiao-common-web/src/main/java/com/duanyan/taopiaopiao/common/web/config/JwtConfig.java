package com.duanyan.taopiaopiao.common.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置
 *
 * @author duanyan
 * @since 1.0.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * 密钥 (至少256位)
     */
    private String secret = "taopiaopiao-secret-key-must-be-at-least-256-bits-long-for-hs256-algorithm";

    /**
     * Token有效期 (秒) - 默认24小时
     */
    private Long expiration = 86400L;

    /**
     * Token前缀
     */
    private String tokenPrefix = "Bearer ";

    /**
     * HTTP Header名称
     */
    private String headerName = "Authorization";
}
